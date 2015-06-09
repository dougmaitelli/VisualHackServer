package com.visualhackserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.visualhackserver.ui.VisualHackServerView;

/**
 *
 * @author DougM
 */
public class FileThread extends Thread {

    private static final FileThread instance = new FileThread(5002);
    private ServerSocket server;
    private ArrayList<TransferThread> files = new ArrayList<TransferThread>();

    private FileThread(Integer port) {
        super("File Thread");
        
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(FileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static FileThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        do {
            try {
                Socket socket = server.accept();

                TransferThread transferThread = new TransferThread(socket);
                transferThread.start();

                addFile(transferThread);
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }
    
    public ArrayList<TransferThread> getFiles() {
        return files;
    }

    public void addFile(TransferThread transferThread) {
        getFiles().add(transferThread);
        getView().addFile(transferThread);
    }

    public void removeFile(TransferThread transferThread) {
        getFiles().remove(transferThread);
        getView().removeFile(transferThread);
    }

    public VisualHackServerView getView() {
        return ServerThread.getInstance().getView();
    }

    public void updateFile(TransferThread transferThread) {
        getView().updateFile(transferThread);
    }
}
