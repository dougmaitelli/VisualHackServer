package com.visualhackserver.thread;

import java.io.IOException;

import com.visualhackserver.client.Client;

/**
 *
 * @author DougM
 */
public class ClientThread extends Thread {

    private Client clientStruct;

    public ClientThread(Client client) {
        super(client.getName());

        clientStruct = client;
    }

    @Override
    public void run() {
        try {
            while (clientStruct.read() != null) {}
        } catch (IOException ex) {
            ServerThread.getInstance().removeClient(clientStruct);
        }
    }

    public Client getClient() {
        return clientStruct;
    }
}
