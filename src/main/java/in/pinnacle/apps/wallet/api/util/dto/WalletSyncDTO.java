package in.pinnacle.apps.wallet.api.util.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletSyncDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4519432224605676145L;

    private String walletId;

    private BigDecimal balance;

}
