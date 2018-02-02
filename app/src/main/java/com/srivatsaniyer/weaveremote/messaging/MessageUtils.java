package com.srivatsaniyer.piremote.messaging;

import com.srivatsaniyer.piremote.messaging.exceptions.BadOperation;
import com.srivatsaniyer.piremote.messaging.exceptions.InvalidMessageStructure;
import com.srivatsaniyer.piremote.messaging.exceptions.MessagingException;
import com.srivatsaniyer.piremote.messaging.exceptions.RequiredFieldsMissing;
import com.srivatsaniyer.piremote.messaging.exceptions.SchemaValidationFailed;
import com.srivatsaniyer.piremote.messaging.exceptions.WaitTimeoutError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class MessageUtils {
    public static <X> void throwExceptionFromMessage(Message<X> msg) throws MessagingException {
        String err = msg.getHeaders().get("RES");
        if ("OK".equalsIgnoreCase(err)) {
            return;
        }

        MessagingException[] arr = {
                new BadOperation(err), new InvalidMessageStructure(err),
                new RequiredFieldsMissing(err), new SchemaValidationFailed(err),
                new WaitTimeoutError(err)
        };
        Map<String, MessagingException> exceptionMap = new HashMap<>();
        for (MessagingException e: arr) {
            exceptionMap.put(e.errorKey(), e);
        }
        MessagingException curException = exceptionMap.get(err);
        if (curException == null) {
            throw new MessagingException(err);
        }
        throw curException;
    }

    public static <X> void ensureOk(Message<X> msg) throws MessagingException {
        if (!msg.getOperation().equals(Operation.RESULT) || !msg.getHeaders().containsKey("RES")) {
            throw new MessagingException("Invalid ACK message.");
        }
        throwExceptionFromMessage(msg);
    }

}
