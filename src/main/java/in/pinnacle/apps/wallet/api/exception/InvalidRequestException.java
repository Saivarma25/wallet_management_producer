package in.pinnacle.apps.wallet.api.exception;

import java.io.Serial;

public class InvalidRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2034909869341629701L;

    public InvalidRequestException(String message) {
        super(message);
    }

}
