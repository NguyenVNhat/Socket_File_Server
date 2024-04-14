package com.example.PBL4.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ClientRequest implements Serializable {
    private String accountRequest;
    private String accountReceive;
    private ArrayList<String> fileChoose = new ArrayList<>();

    public ClientRequest(String accountRequest, String accountReceive, ArrayList<String> fileChoose) {
        this.accountRequest = accountRequest;
        this.accountReceive = accountReceive;
        this.fileChoose = fileChoose;
    }

    public String getAccountRequest() {
        return accountRequest;
    }

    public void setAccountRequest(String accountRequest) {
        this.accountRequest = accountRequest;
    }

    public String getAccountReceive() {
        return accountReceive;
    }

    public void setAccountReceive(String accountReceive) {
        this.accountReceive = accountReceive;
    }

    public ArrayList<String> getFileChoose() {
        return fileChoose;
    }

    public void setFileChoose(ArrayList<String> fileChoose) {
        this.fileChoose = fileChoose;
    }
}
