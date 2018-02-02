package com.srivatsaniyer.piremote.messaging.exceptions;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class QueueNotFound extends MessagingException {
    public QueueNotFound(String reason) {
        super(reason);
    }
}
