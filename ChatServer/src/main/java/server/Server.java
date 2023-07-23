package main.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This server uses TCP/IP socket to echoes every message from client.
 * This server is multithreaded.
*/
public class Server {
    private int port;
    private List<ConnectionHandler> connections;

    public Server(int port) {
        this.port = port;
        connections = new ArrayList<>();
        startServer();
    }

    private void startServer() {
        try(ServerSocket serverSocket =  new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while(true) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connection = new ConnectionHandler(socket);
                connections.add(connection);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
