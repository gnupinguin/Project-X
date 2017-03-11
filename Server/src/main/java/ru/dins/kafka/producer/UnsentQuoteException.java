package ru.dins.kafka.producer;

/**
 * An exception occurred when getting an inability to send a quote.
 */
public class UnsentQuoteException extends Exception {

    public UnsentQuoteException() {
    }

    public UnsentQuoteException(String message) {
        super(message);
    }

    public UnsentQuoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsentQuoteException(Throwable cause) {
        super(cause);
    }

    public UnsentQuoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
