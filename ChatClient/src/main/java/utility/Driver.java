package main.java.utility;

import main.java.client.Client;

public class Driver {
    public static void main(String[] args) {
        Client client = new Client("localhost", 1234);
        client.broadcastMessage();
        client.listenForMessages();
    }
}
