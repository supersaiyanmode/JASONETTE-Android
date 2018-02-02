package com.srivatsaniyer.weaveremote.messaging.exceptions;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class SchemaValidationFailed extends MessagingException {
    public SchemaValidationFailed(String reason) {
        super(reason);
    }
}
