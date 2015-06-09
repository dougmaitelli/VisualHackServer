package com.visualhackserver.thread;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.visualhackserver.util.Md5;

/**
 *
 * @author DougM
 */
public class TransferThread extends Thread {

    private Socket fileSocket;
    private String fileName;
    private long size;
    private String hashCode;
    private long totalReceived = 0;
    private int status = 0;
    public static final int COMPLETE = 1;
    public static final int ERROR = 2;

    public TransferThread(Socket socket) {
        super("Transfer Thread");

        fileSocket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = fileSocket.getInputStream();

            BufferedReader isr = new BufferedReader(new InputStreamReader(is));

            fileName = isr.readLine();
            size = Long.parseLong(isr.readLine());
            hashCode = isr.readLine();

            FileOutputStream fos = new FileOutputStream(fileName);

            byte[] bytes = new byte[fileSocket.getReceiveBufferSize()];
            
            int bytesReceived = 0;

            while ((bytesReceived = is.read(bytes)) > 0) {
                fos.write(bytes, 0, bytesReceived);

                totalReceived += bytesReceived;
                FileThread.getInstance().updateFile(this);
            }

            fos.close();

            Md5 hash = new Md5(fileName);
            if (hash.compare(hashCode)) {
                status = COMPLETE;
            } else {
                status = ERROR;
            }

            FileThread.getInstance().updateFile(this);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancel() {
        interrupt();
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public String getHashCode() {
        return hashCode;
    }

    public long getTotalReceived() {
        return totalReceived;
    }

    public int getStatus() {
        return status;
    }
}
