package in.pinnacle.apps.wallet.api.controller;

import in.pinnacle.apps.wallet.api.exception.InvalidRequestException;
import in.pinnacle.apps.wallet.api.exception.WalletException;
import in.pinnacle.apps.wallet.api.util.dto.WalletDTO;
import in.pinnacle.apps.wallet.api.util.dto.WalletTransactionDTO;
import in.pinnacle.apps.wallet.api.service.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    /**
     * Controller method to get wallet balance by calling getWalletMasterBalance service method
     * @param walletId ID of the wallet to get wallet details
     * @return ResponseEntity<?> which can have wallet details, else an error message
     */
    @GetMapping("/getBalance")
    public ResponseEntity<Object> getWalletBalance(@RequestParam @NotNull Long walletId) {
        return ResponseEntity.ok(walletService.getWalletBalance(walletId));
    }

    /**
     * Controller method to create a wallet by calling createWalletMaster service method
     * @param walletDTO A util that holds basic details that are needed to create wallet master
     * @return ResponseEntity<?> which can have wallet created, else an error message
     */
    @PostMapping("/createWallet")
    public ResponseEntity<Object> createWallet(@RequestBody @Valid WalletDTO walletDTO) {
        return ResponseEntity.ok(walletService.createWalletMaster(walletDTO));
    }

    /**
     * Controller method to add/debit amount to wallet by calling updateWalletAmount service method
     * @param walletTransactionDTO A util that contains basic details to create transaction
     * @return ResponseEntity<?> which can have wallet details after transaction, else an error message
     */
    @PutMapping("/addTransaction")
    public ResponseEntity<Object> addWalletTransaction(@RequestBody @Valid WalletTransactionDTO walletTransactionDTO) {
        return ResponseEntity.ok(walletService.createTransactions(walletTransactionDTO));
    }

}