package excel.exeptions;

/**
 * Created by Belousov on 14.09.2016.
 */
public class WrongFunctionExeption extends RuntimeException {
    public WrongFunctionExeption() {
    }

    public WrongFunctionExeption(String message) {
        super(message);
    }

    public WrongFunctionExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFunctionExeption(Throwable cause) {
        super(cause);
    }

    public WrongFunctionExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
