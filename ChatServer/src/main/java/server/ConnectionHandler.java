package main.java.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible to handle client connection.
 */
public class ConnectionHandler implements Runnable{
    private static List<ConnectionHandler> connections = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientUsername;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        connections.add(this);
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            writer.println("Please enter a nickname: ");
            clientUsername = reader.readLine();

            System.out.println(clientUsername + " is been connected");
            writer.println(clientUsername + " joined to chatroom");

            String message;

            while(socket.isConnected()) {
                message = reader.readLine();

                if(message.startsWith("/quit")) {
                    removeConnectionHandler();
                    shutdown();
                }
                else {
                    broadcastMessage(clientUsername + ": " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    /***
     * This method send a broadcast message except to the user who wrote it.
     * @param message - the broadcast message.
     */
    private void broadcastMessage(String message) {
        for (ConnectionHandler connection : connections) {
            if(!connection.clientUsername.equals(clientUsername)) {
                writer.println(message);
            }
        }
    }

    /**
     * This method removes the disconnected client
     * and notify all the users.
     */
    private void removeConnectionHandler() {
        connections.remove(this);
        broadcastMessage("Server: " + clientUsername + " left the chatroom!");
    }

    /**
     * This method close the client connection,
     * removes the client from clients list and
     * notify all the users.
     */
    private void shutdown() {
        removeConnectionHandler();

        try {
            reader.close();
            writer.close();

            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
