package in.pinnacle.apps.wallet.api.service;

import in.pinnacle.apps.wallet.api.exception.WalletException;
import in.pinnacle.apps.wallet.api.util.dto.WalletDTO;
import in.pinnacle.apps.wallet.api.util.dto.WalletSyncDTO;
import in.pinnacle.apps.wallet.api.util.dto.WalletTransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<Long, WalletTransactionDTO> walletTransactionkafkaTemplate;

    private final KafkaTemplate<Long, Object> walletkafkaTemplate;

    private static final String WALLET_TRANSACTION = "wallet_transaction";

    private static final String WALLET = "wallet";

    private static final String WALLET_SYNC = "wallet_sync";

    public KafkaProducerService(@Qualifier("walletTransactionKafkaTemplate") KafkaTemplate<Long,
                                        WalletTransactionDTO> walletTransactionkafkaTemplate,
                                @Qualifier("walletKafkaTemplate") KafkaTemplate<Long, Object> walletKafkaTemplate) {
        this.walletTransactionkafkaTemplate = walletTransactionkafkaTemplate;
        this.walletkafkaTemplate = walletKafkaTemplate;
    }

    /**
     * Method to send transaction details to kafka which pushes in fire and forgot approach
     * @param walletTransactionDTO that contains basic details to push a transaction to consumer
     */
    public void sendWalletTransactionMessage(WalletTransactionDTO walletTransactionDTO) {
        try {
            walletTransactionkafkaTemplate.send(WALLET_TRANSACTION, walletTransactionDTO.getWalletId(), walletTransactionDTO)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send transaction message: {}", ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            /*
            Redis amount can be reverted and new transaction request should be sent from
            here or from UI....
             */
            log.error("No connection to kafka", e);
            throw new WalletException("Kafka service not available");
        }
    }

    /**
     * Producer method to send create wallet message to consumer
     * @param walletDTO DTO used to send Wallet util details to consumer application
     */
    public void sendCreateWalletMessage(WalletDTO walletDTO) {
        try {
            walletkafkaTemplate.send(WALLET, walletDTO).get();
        } catch (ExecutionException e) {
            log.error("Failed to send create wallet message for user id: ", e);
            throw new WalletException("Failed to send create wallet message for user id: " + e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Kafka send interrupted while waiting for acknowledgment: ", e);
            throw new WalletException("Kafka send interrupted while waiting for acknowledgment: " + e.getMessage());
        } catch (Exception e) {
            log.error("Kafka service not available:", e);
            throw new WalletException("Kafka service not available:" + e.getMessage());
        }
    }

    /**
     * Producer method to send wallet sync to kafka
     * @param walletSyncDTO DTO for wallet sync
     */
    public void sendWalletSyncMessage(WalletSyncDTO walletSyncDTO) {
        try {
            walletkafkaTemplate.send(WALLET_SYNC, walletSyncDTO).get();
        } catch (ExecutionException e) {
            log.error("Failed to send wallet sync message: ", e);
            throw new WalletException("Failed to send wallet sync message: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Kafka send interrupted while waiting for acknowledgment: ", e);
            throw new WalletException("Kafka send interrupted while waiting for acknowledgment: " + e.getMessage());
        } catch (Exception e) {
            log.error("Kafka service not available: ", e);
            throw new WalletException("Kafka service not available: " + e.getMessage());
        }
    }

}
