package main.java.utility;

import main.java.server.Server;

public class Driver {
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.startServer();
    }
}
