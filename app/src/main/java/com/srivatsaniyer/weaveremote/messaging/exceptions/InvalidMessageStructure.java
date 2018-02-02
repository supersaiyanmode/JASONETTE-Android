package com.srivatsaniyer.piremote.messaging.exceptions;

/**
 * Created by sriiyer on 9/23/17.
 */

public class InvalidMessageStructure extends MessagingException {
    public InvalidMessageStructure(String reason) {
        super(reason);
    }

}
