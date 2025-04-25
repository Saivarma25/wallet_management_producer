package in.pinnacle.apps.wallet.api.util.dto;

import in.pinnacle.apps.wallet.api.util.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletTransactionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2012725756555899488L;

    /**
     * ID of the wallet for which this transaction belongs to
     */
    @NotNull
    private Long walletId;

    /**
     * ID of the user for which this wallet and transaction belongs to
     */
    private Long userId;

    /**
     * Amount of the transaction requested
     */
    @NotNull
    private double transactionAmount;

    /**
     * Total amount of wallet after adding transaction amount
     */
    private BigDecimal walletAmount;

    /**
     * Type of the transaction requested
     */
    @NotNull
    private TransactionType transactionType;

    /**
     * Details about the transaction requested
     */
    @NotBlank
    private String description;

    /**
     * Date at which the transaction happened
     */
    private LocalDateTime createdAt;

}
