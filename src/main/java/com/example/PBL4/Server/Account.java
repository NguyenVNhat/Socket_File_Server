package com.example.PBL4.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Account implements Serializable  {
    private String Username;
    private transient Socket socket;
    private ArrayList<String> lisFileName = new ArrayList<>();
    private transient ObjectOutputStream objectOutputStream;
    private transient ObjectInputStream objectInputStream;

    public Account(String username, Socket socket, ArrayList<String> lisFileName, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        Username = username;
        this.socket = socket;
        this.lisFileName = lisFileName;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ArrayList<String> getLisFileName() {
        return lisFileName;
    }

    public void setLisFileName(ArrayList<String> lisFileName) {
        this.lisFileName = lisFileName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
