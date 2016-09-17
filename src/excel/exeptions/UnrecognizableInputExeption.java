package excel.exeptions;

/**
 * Created by Belousov on 14.09.2016.
 */
public class UnrecognizableInputExeption extends RuntimeException {
    public UnrecognizableInputExeption() {
    }

    public UnrecognizableInputExeption(String message) {
        super(message);
    }

    public UnrecognizableInputExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public UnrecognizableInputExeption(Throwable cause) {
        super(cause);
    }

    public UnrecognizableInputExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
