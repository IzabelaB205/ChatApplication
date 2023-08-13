package main.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This server uses TCP/IP socket to echoes every message from client.
 * This server is multithreaded.
*/
public class Server {
    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    /**
     * This method start the server by listening to port.
     * To support multiple clients each connection runs in a new thread.
     */
    public void startServer() {
        try {
            serverSocket =  new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connection = new ConnectionHandler(socket);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    /**
     * This method shutdown the server
     * by closing the ServerSocket connection.
     */
    private void shutdown() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
