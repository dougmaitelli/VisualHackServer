package com.visualhackserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author DougM
 */
public class Md5 {

    private String hashCode;

    public Md5(String fileName) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        MessageDigest hash = MessageDigest.getInstance("MD5");

        byte[] bytes = new byte[1024];

        int bytesRead = 0;
        while ((bytesRead = fis.read(bytes)) != -1) {
            hash.update(bytes, 0, bytesRead);
        }
        
        fis.close();

        byte[] b = hash.digest();

        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            hex.append(Integer.toHexString((b[i] & 0xff) + 0x100).substring(1));
        }

        hashCode = hex.toString();
    }

    public String getHash() {
        return hashCode;
    }

    public Boolean compare(String hash) {
        return hashCode.equals(hash);
    }
}
