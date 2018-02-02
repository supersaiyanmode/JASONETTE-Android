package com.srivatsaniyer.weaveremote.messaging.exceptions;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class WaitTimeoutError extends MessagingException {
    public WaitTimeoutError(String reason) {
        super(reason);
    }
}
