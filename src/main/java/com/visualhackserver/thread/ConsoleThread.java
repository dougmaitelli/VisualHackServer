package com.visualhackserver.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.visualhackserver.client.Client;
import com.visualhackserver.client.console.ClientConsole;

/**
 *
 * @author DougM
 */
public class ConsoleThread extends Thread {

    private Client clientStruct;
    private Socket consoleSocket;
    private ClientConsole clientConsole;
    private BufferedReader in;
    private PrintWriter out;

    public ConsoleThread(Client client) throws IOException {
        super("Console");

        clientStruct = client;

        ServerSocket consoleServer = new ServerSocket(0);

        Integer port = consoleServer.getLocalPort();

        clientStruct.send(port.toString());

        consoleSocket = consoleServer.accept();

        clientConsole = new ClientConsole(this, clientStruct);

        ServerThread.getInstance().getView().addConsole(clientConsole);

        in = new BufferedReader(new InputStreamReader(consoleSocket.getInputStream()));
        out = new PrintWriter(consoleSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            int buffer;
            while ((buffer = in.read()) != -1) {
                clientConsole.write(Character.toString((char) buffer));
            }
        } catch (IOException ex) {
            interrupt();
        }
    }
    
    @Override
    public void interrupt() {
    	try {
			consoleSocket.close();
			
			clientConsole.setEnabled(false);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    	
    	super.interrupt();
    }
    
    public Socket getSocket() {
        return consoleSocket;
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

    public ClientConsole getConsole() {
        return clientConsole;
    }
}
