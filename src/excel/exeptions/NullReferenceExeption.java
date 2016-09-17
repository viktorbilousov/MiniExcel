package excel.exeptions;

/**
 * Created by Belousov on 14.09.2016.
 */
public class NullReferenceExeption extends RuntimeException {

    public NullReferenceExeption() {
    }

    public NullReferenceExeption(String message) {
        super(message);
    }

    public NullReferenceExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public NullReferenceExeption(Throwable cause) {
        super(cause);
    }

    public NullReferenceExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
