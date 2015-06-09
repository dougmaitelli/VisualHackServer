package com.visualhackserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.visualhackserver.VisualHackServerApp;
import com.visualhackserver.client.Client;
import com.visualhackserver.ui.VisualHackServerView;

/**
 *
 * @author DougM
 */
public class ServerThread extends Thread {

    private static final ServerThread instance = new ServerThread(5001);
    private ServerSocket server;
    private ArrayList<Client> clients = new ArrayList<Client>();

    private ServerThread(Integer port) {
        super("Server Thread");
        
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ServerThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        FileThread.getInstance().start();

        do {
            try {
                Socket socket = server.accept();

                Client client = new Client(socket);
                client.setName(client.getInputStream().readLine());

                addClient(client);

                ClientThread clientThread = new ClientThread(client);
                clientThread.start();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        getClients().add(client);
        getView().addClient(client);
    }

    public void removeClient(Client client) {
        getClients().remove(client);
        getView().removeClient(client);
    }

    public VisualHackServerView getView() {
        return (VisualHackServerView) VisualHackServerApp.getApplication().getMainView();
    }
}
