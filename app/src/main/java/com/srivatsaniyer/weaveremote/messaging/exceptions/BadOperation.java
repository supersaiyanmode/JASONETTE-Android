package com.srivatsaniyer.weaveremote.messaging.exceptions;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class BadOperation extends MessagingException {
    public BadOperation(String reason) {
        super(reason);
    }
}
