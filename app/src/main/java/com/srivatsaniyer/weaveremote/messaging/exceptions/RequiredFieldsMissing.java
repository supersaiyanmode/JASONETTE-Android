package com.srivatsaniyer.weaveremote.messaging.exceptions;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class RequiredFieldsMissing extends MessagingException {
    public RequiredFieldsMissing(String reason) {
        super(reason);
    }
}
