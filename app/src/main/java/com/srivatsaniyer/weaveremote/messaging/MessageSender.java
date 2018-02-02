package com.srivatsaniyer.weaveremote.messaging;

import com.srivatsaniyer.weaveremote.messaging.exceptions.MessagingException;

import java.io.IOException;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class MessageSender {
    public MessageSender(String queueName, MessagingClient client) throws IOException {
        this.client = client;
        this.queueName = queueName;
    }

    public <T> void send(T obj) throws IOException, MessagingException {
        Message<T> msg = new Message<T>(Operation.ENQUEUE, obj);
        msg.getHeaders().put("Q", this.queueName);
        this.client.writeMessage(msg);

        Message<Void> resp = this.client.<Void>readMessage(null);
        MessageUtils.ensureOk(resp);
    }


    private MessagingClient client;
    private String queueName;
}
