package main.java.server;

import java.io.*;
import java.net.Socket;

/**
 * This class is responsible to handle client connection.
 */
public class ConnectionHandler extends Thread{
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("Please enter a nickname: ");
            String nickname = reader.readLine();

            //TODO: check if nickname already exists

            System.out.println(nickname + " is been connected");
            writer.println(nickname + " joined to chatroom");

            String message;

            while((message = reader.readLine()) != null) {
                writer.println(nickname + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
