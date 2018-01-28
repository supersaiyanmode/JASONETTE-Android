package com.srivatsaniyer.piremote.messaging;

import com.srivatsaniyer.piremote.messaging.exceptions.BadOperation;

/**
 * Created by sriiyer on 9/23/17.
 */

public enum Operation {
    ENQUEUE("enqueue"),
    DEQUEUE("dequeue"),
    INFORM("inform"),
    RESULT("result");

    public String serialize() {
        return this.tag;
    }

    public static Operation parse(String op) throws BadOperation {
        if ("enqueue".equalsIgnoreCase(op)) {
            return Operation.ENQUEUE;
        } else if ("dequeue".equalsIgnoreCase(op)) {
            return Operation.DEQUEUE;
        } else if ("inform".equalsIgnoreCase(op)) {
            return Operation.INFORM;
        } else if ("result".equalsIgnoreCase(op)) {
            return Operation.RESULT;
        }
        throw new BadOperation(op);
    }

    Operation(String tag) {
        this.tag = tag;
    }

    private final String tag;
}
