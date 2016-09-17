package excel.exeptions;


/**
 * Created by Belousov on 14.09.2016.
 */
public class CyclicReferenceExeption extends RuntimeException {

    public CyclicReferenceExeption() {
    }

    public CyclicReferenceExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public CyclicReferenceExeption(Throwable cause) {
        super(cause);
    }

    public CyclicReferenceExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CyclicReferenceExeption(String message) {

        super(message);
    }
}

