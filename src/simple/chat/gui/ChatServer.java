package simple.chat.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer extends Application {

    private List<ChatThread> threads = new ArrayList<>();
    private Set<String> usernames = new HashSet<>();

    @Override
    public void start(Stage stage) throws Exception {
    }

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(5401)) {
            System.out.println("Chat Server is listening on port 5401.");
            ChatServer server = new ChatServer();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected.");
                System.out.println(server.threads.size());
                Platform.runLater(() -> {
                    try {
                        ChatThread thread = new ChatThread(socket, server);
                        server.threads.add(thread);
                        thread.call();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public boolean hasUsers() {
        return threads.size() > 0;
    }

    public Set<String> getUsernames() {
        return usernames;
    }
    public void addUsername(String username) {
        usernames.add(username);
    }

    public void broadcast(String message, ChatThread sender) {
        for (ChatThread thread : threads) {
            if (!thread.getUsername().equals(sender.getUsername())) {
                thread.writeLine(message);
            }
        }
    }

    public void broadcast(String message) {
        for (ChatThread thread : threads) {
            thread.writeLine(message);
        }
    }

    public void removeUsername(String username) {
        usernames.remove(username);
    }

    public void removeThread(ChatThread thread) {
        threads.remove(thread);
    }
}
