package com.visualhackserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.visualhackserver.thread.ConsoleThread;

/**
 *
 * @author DougM
 */
public class Client {

    private String clientName;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) throws IOException {
        clientSocket = socket;

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public String getName() {
        return clientName;
    }

    public void setName(String name) {
        clientName = name;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public BufferedReader getInputStream() {
        return in;
    }

    public PrintWriter getOutputStream() {
        return out;
    }

    public void send(String str) {
        getOutputStream().println(str);
    }

    public void requestConsole() throws IOException {
        ConsoleThread console = new ConsoleThread(this);
        console.start();
    }
}
