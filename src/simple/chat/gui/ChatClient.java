package simple.chat.gui;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    public static void main(String[] args) throws IOException {
        new Socket("127.0.0.1", 5401);
        System.out.println("Connected to the chat server");
    }
}