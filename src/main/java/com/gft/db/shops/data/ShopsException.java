package com.gft.db.shops.data;

public class ShopsException extends Exception {

    public ShopsException() {
        super();
    }

    public ShopsException(final String message) {
        super(message);
    }

    public ShopsException(final Exception exception) {
        super(exception);
    }
}
