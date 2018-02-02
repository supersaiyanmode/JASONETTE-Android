package com.srivatsaniyer.weaveremote.messaging.exceptions;

/**
 * Created by sriiyer on 9/23/17.
 */

public class MessagingException extends Exception {
    public MessagingException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public MessagingException(String reason) {
        super(reason);
    }

    public String errorKey() {
        return this.getClass().getSimpleName();
    }
}
