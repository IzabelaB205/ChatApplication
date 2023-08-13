package main.java.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public Client(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer= new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * This method gets the broadcast messages from server.
     * The message been printed to the command line.
     */
    public void listenForMessages() {
        String message;

        while(socket.isConnected()) {
            try {
                if((message = reader.readLine()) != null) {
                    System.out.println(message);
                }

            } catch (IOException e) {
                shutdown();
                break;
            }
        }
    }

    /**
     * This method reads the user input message and
     * sent it to the server.
     */
    public void broadcastMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                    username = inReader.readLine();
                    writer.println(username);

                    String message;

                    while(socket.isConnected()) {
                        message = inReader.readLine();
                        writer.println(message);

                        if(message.equals("/quit")) {
                            inReader.close();
                            shutdown();
                            break;
                        }
                    }
                } catch (IOException e) {
                    shutdown();
                }
            }
        }).start();
    }

    /**
     * This method shutdown the client
     * by closing the Socket connection with the server.
     */
    private void shutdown() {
        try {
            if(reader != null) reader.close();

            if(writer != null) writer.close();

            if(!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
