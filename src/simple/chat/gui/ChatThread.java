package simple.chat.gui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
public class ChatThread extends Task {
    private final ChatServer server;
    private Socket socket;
    private String username;
    private TextArea area;
    private TextField field;
    private Button button;
    private Stage stage = new Stage();

    public ChatThread(Socket socket, ChatServer server) throws IOException {
        this.socket = socket;
        this.server = server;
    }
    
    @Override
    protected Object call() {
        //The application will send a message to everyone else and notify the user of the rest of the users.

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("What is your name?");
        username = dialog.showAndWait().orElse("");
        area = new TextArea();
        area.setEditable(false);

        area.setLayoutX(10);
        area.setLayoutY(10);
        area.setPrefWidth(600);
        area.setPrefHeight(600);

        field = new TextField();
        field.setPromptText("Type a message...");
        field.setLayoutX(10);
        field.setLayoutY(620);
        field.setPrefHeight(30);
        field.setPrefWidth(530);

        button = new Button("Send");
        button.setLayoutX(550);
        button.setLayoutY(620);
        button.setPrefWidth(50);
        button.setPrefHeight(30);

        Group group = new Group(new ScrollPane(area), field, button);
        Scene scene = new Scene(group);

        stage.setWidth(650);
        stage.setHeight(700);

        stage.setTitle("Chat: " + username);
        stage.setScene(scene);

        stage.show();
        button.setOnAction(e -> {
            server.broadcast(username + ": " + field.getText() + "\n");
            field.setText("");
        });

        server.broadcast(username + " has connected.", this);
        stage.setOnCloseRequest(e -> {
            server.broadcast(username + " has left.\n");
            field.setText("");
            server.removeUsername(username);
            server.removeThread(this);
        });
        server.addUsername(username);
        System.out.println(server.getUsernames().size());

        button.setOnAction(e -> {
            //How to get the server to broadcast the message?
            server.broadcast(username + ": " + field.getText());
            field.setText("");
        });
        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public Button getButton() {
        return button;
    }

    public TextArea getArea() {
        return area;
    }

    public TextField getField() {
        return field;
    }


    public Stage getStage() {
        return stage;
    }

    public void writeLine(String message) {
        area.appendText(message + "\n");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
