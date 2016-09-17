package excel.exeptions;

/**
 * Created by Belousov on 14.09.2016.
 */
public class UnrecognuzableReferenceExeption extends RuntimeException {
    public UnrecognuzableReferenceExeption() {
    }

    public UnrecognuzableReferenceExeption(String message) {
        super(message);
    }

    public UnrecognuzableReferenceExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public UnrecognuzableReferenceExeption(Throwable cause) {
        super(cause);
    }

    public UnrecognuzableReferenceExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
