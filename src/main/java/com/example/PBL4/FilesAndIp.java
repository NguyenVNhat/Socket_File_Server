package com.example.PBL4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FilesAndIp implements Serializable {
    private ArrayList<String> ListFileName = new ArrayList<>();
    private String IpClient = new String();
    private String username;
    public FilesAndIp()
    {
        this.IpClient = new String();
        this.ListFileName = new ArrayList<>();
        this.username = new String();
    }
    public FilesAndIp(ArrayList<String> listFileName, String ipClient,String username)
    {
        this.ListFileName = listFileName;
        this.IpClient = ipClient;
        this.username = username;
    }

    public ArrayList<String> getListFileName() {
        return ListFileName;
    }

    public void setListFileName(ArrayList<String> listFileName) {
        ListFileName = listFileName;
    }

    public String getIpClient() {
        return IpClient;
    }

    public void setIpClient(String ipClient) {
        IpClient = ipClient;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilesAndIp)) return false;
        FilesAndIp that = (FilesAndIp) o;
        return Objects.equals(ListFileName, that.ListFileName) &&
                Objects.equals(IpClient, that.IpClient) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ListFileName, IpClient, username);
    }
}
