package in.pinnacle.apps.wallet.api.service;

import in.pinnacle.apps.wallet.api.exception.InvalidRequestException;
import in.pinnacle.apps.wallet.api.util.dto.WalletDTO;
import in.pinnacle.apps.wallet.api.util.dto.WalletSyncDTO;
import in.pinnacle.apps.wallet.api.util.dto.WalletTransactionDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final RedisTemplate<String, BigDecimal> redisTemplate;

    private final KafkaProducerService kafkaProducerService;

    private static final String WALLET = "wallet:";

    /**
     * Method to get the wallet balance with the given wallet id
     * @param walletId ID of the wallet to fetch record from cache or db
     * @return WalletDTO object that has wallet ID with balance
     */
    public WalletDTO getWalletBalance(@NotNull Long walletId) {
        BigDecimal cachedBalance = redisTemplate.opsForValue().get(WALLET + walletId);
        if (cachedBalance == null)
            throw new InvalidRequestException("No wallet available with given wallet id");

        return new WalletDTO(walletId, cachedBalance);
    }

    /**
     * Method to create a wallet with given details
     * @param walletDTO dto that contains wallet details
     * @return Wallet dto object if kafka send is successful, null otherwise
     */
    public WalletDTO createWalletMaster(@NotNull WalletDTO walletDTO) {
        walletDTO.setCreatedAt(LocalDateTime.now());
        kafkaProducerService.sendCreateWalletMessage(walletDTO);
        return walletDTO;
    }

    /**
     * Method to createATransaction which can be negative or positive
     * @param walletTransactionDTO util that holds basic details to make a transaction
     * @return WalletDTO object that has updated details after transaction
     */
    public WalletTransactionDTO createTransactions(@NotNull WalletTransactionDTO walletTransactionDTO) {
        WalletDTO walletDTO = this.getWalletBalance(walletTransactionDTO.getWalletId());
        if (walletDTO == null) throw new InvalidRequestException("No wallet found with given details");

        // Immediately updated redis for thread safety total check
        double newAmount = Objects.requireNonNull(redisTemplate.opsForValue().increment(
                WALLET + walletDTO.getWalletId(), walletTransactionDTO.getTransactionAmount() *
                        walletTransactionDTO.getTransactionType().getMultiplier()));
        if (newAmount < 0)
            // If new total is less than zero revert redis amount which is also thread safe
            redisTemplate.opsForValue().increment(WALLET + walletTransactionDTO.getWalletId(),
                    -walletTransactionDTO.getTransactionAmount() *
                            walletTransactionDTO.getTransactionType().getMultiplier());

        // Send transaction to kafka(fire and forgot)
        walletTransactionDTO.setWalletAmount(BigDecimal.valueOf(newAmount));
        walletTransactionDTO.setCreatedAt(LocalDateTime.now());
        kafkaProducerService.sendWalletTransactionMessage(walletTransactionDTO);
        return walletTransactionDTO;
    }

    /**
     * Method to send wallet balances to consumer for sync purpose
     */
    public void sendWalletSyncFromRedis() {
        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(WALLET + "*").count(1000).build());
        cursor.forEachRemaining(key -> {
            BigDecimal balance = redisTemplate.opsForValue().get(key);
            if (balance != null) {
                String walletId = key.replace(WALLET, "");
                kafkaProducerService.sendWalletSyncMessage(new WalletSyncDTO(walletId, balance));
            }
        });
    }

}