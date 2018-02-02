package com.srivatsaniyer.piremote.messaging;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.srivatsaniyer.piremote.messaging.exceptions.MessagingException;

import java.lang.reflect.Type;

/**
 * Created by thrustmaster on 9/30/17.
 */

public abstract class MessageReceiver<T> {
    public MessageReceiver(String queueName, MessagingClient client, Type mainType, Type... types) {
        this.client = client;
        this.queueName = queueName;
        this.active = true;

        this.mainType = mainType;
        this.typeArgs = types;

        final MessageReceiver receiver = this;
        this.receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                receiver.run();
            }
        });
    }

    public void start() {
        this.receiverThread.start();
    }

    public void run() {
        while (this.active) {
            Message<Void> dequeueMsg = new Message<Void>(Operation.DEQUEUE);
            dequeueMsg.getHeaders().put("Q", this.queueName);
            client.writeMessage(dequeueMsg);
            try {
                Type messageType = TypeToken.getParameterized(mainType, typeArgs).getType();
                this.onMessage(client.<T>readMessage(messageType));
            } catch (MessagingException e) {
                Log.w("MessageReceiver", "Got an exception.", e);
            }
        }
    }

    public abstract void onMessage(Message<T> msg);

    public void stop() {
        this.active = false;
        this.receiverThread.interrupt();
    }


    private final MessagingClient client;
    private final String queueName;
    private volatile boolean active;
    private final Thread receiverThread;
    private final Type mainType;
    private final Type[] typeArgs;
}
