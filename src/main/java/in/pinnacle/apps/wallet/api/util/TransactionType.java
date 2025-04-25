package in.pinnacle.apps.wallet.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    CREDIT(1),
    DEBIT(-1);

    private final int multiplier;

}
