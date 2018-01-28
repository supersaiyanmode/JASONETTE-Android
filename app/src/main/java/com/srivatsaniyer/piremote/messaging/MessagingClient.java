package com.srivatsaniyer.piremote.messaging;

import com.srivatsaniyer.piremote.messaging.exceptions.MessagingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;

public class MessagingClient {
    public MessagingClient(ServerSpecification spec) throws IOException {
        socket = new Socket(spec.getHost(), spec.getPort());
        writer = new PrintWriter(socket.getOutputStream()) {
            @Override
            public void println() {
                write('\n');
            }
        };
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void writeMessage(Message msg) {
        msg.write(writer);
        writer.flush();
    }

    public <T> Message<T> readMessage(Type type) throws MessagingException {
        return Message.<T>read(reader, type);
    }

    public void close() throws IOException {
        writer.flush();
        socket.close();
    }

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static String TAG = "MessagingClient";
}
