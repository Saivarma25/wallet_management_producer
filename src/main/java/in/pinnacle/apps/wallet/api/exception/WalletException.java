package in.pinnacle.apps.wallet.api.exception;

import java.io.Serial;

public class WalletException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7744878177560520817L;

    public WalletException(String message) {
        super(message);
    }

}
