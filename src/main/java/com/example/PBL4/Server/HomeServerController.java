package com.example.PBL4.Server;

import com.example.PBL4.FilesAndIp;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomeServerController {
    @FXML
    private Button buttonOpenServer;
    @FXML
    private TableView tableFile;
    @FXML
    private TableColumn<MyColumn,String> columnName;
    @FXML
    private TableColumn<MyColumn,String> columnExtension;
    @FXML
    private TextField PortField;
    @FXML
    private TextField IPField;
    @FXML
    private TableView tableClient;
    @FXML
    private TableView tableListRequest;
    @FXML
    private TableColumn<ClientTableColumn,String> columNameCLient;
    @FXML
    private TableColumn<ClientTableColumn,String> columnSocketCLient;
    @FXML
    private TableColumn<ClientTableColumn,String> columnSocketPort;
    @FXML
    private TableColumn<ClientTableColumn,String> columnSocketPort2;
    @FXML
    private TableColumn<ClientRequestColumn,String> columnNameCLientRequest;
    @FXML
    private TableColumn<ClientRequestColumn,String> columnIPCLientRequest;
    @FXML
    private TableColumn<ClientRequestColumn,Button> columnViewCLientRequest;
    @FXML
    private TableColumn<ClientRequestColumn,Boolean> columnAcceptCLientRequest;
    @FXML
    private TextField numberAccount;
    @FXML
    private TextField numberAccountConnect;
    @FXML
    private TextField timeUpdate;
    @FXML
    private Button buttonSetting;
    @FXML
    private AnchorPane anchorpaneThongbao;
    private ServerSocket serverSocket;
    private ServerSocket serverSocket1 = new ServerSocket(8889);

    private  ArrayList<FilesAndIp> listRequest = new ArrayList<>();
    private ArrayList<ObjectOutputStream> objectOutputStreamArrayList = new ArrayList<>();


    private List<Account> listAccount = new ArrayList<>();
    public HomeServerController() throws IOException {

    }
    public String getIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        String TempmyIP = ip.toString();
        String[] parts = TempmyIP.split("/");
        String myIP = "";
        if (parts.length > 1) {
            myIP = parts[1];
        }
        return myIP;
    }
    private  ArrayList<String> listFile = new ArrayList<>();
    private Date nextTime = null;
    private Date lastTime = null;
    private Integer numberDay;
    private Date NextTime;
    private Date LastTime;
    private Integer numberTime;

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
        String USER_NAME = "root";
        String PASSWORD = "";
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        return conn;
    }
    private void getTime() throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String queryTime = "select * from timetosynchnorou ";
        ResultSet rsTime = stmt.executeQuery(queryTime);
        while (rsTime.next())
        {
            NextTime = rsTime.getDate("NextTime");
            LastTime = rsTime.getDate("LastTime");
            numberTime = rsTime.getInt("Time");
        }
    }

    @FXML
    public void startServer() throws IOException, ClassNotFoundException, SQLException {

        try {
            getTime();
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String query = "select * from Account";

            ResultSet rs = stmt.executeQuery(query);
            Integer numberAccount = 0;
            while (rs.next()) {
                numberAccount++;
            }
            //ViewTime();
            this.numberAccount.setText(numberAccount.toString());
            this.numberAccountConnect.setText("0");
            serverSocket = new ServerSocket(Integer.parseInt(PortField.getText()));
            IPField.setText(getIP());
            buttonOpenServer.setText("Đã mở");

            File  directory = new File("Data\\");
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if(!file.getName().equals("End.txt"))
                        listFile.add(file.getName());
                }
            }

            receiveData(listFile);
            Thread threadRun = new Thread(() ->{
                while (true) {
                    Socket socket = null;
                    try {
                        socket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(socket, listFile, listRequest,listAccount,numberAccountConnect);
                        clientHandler.setData(listFile,serverSocket1,objectOutputStreamArrayList);
                        new Thread(clientHandler).start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            threadRun.start();
            buttonOpenServer.setDisable(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server chưa kết nối cơ sở dữ liệu");
        }


    }
    ObservableList<MyColumn> tableData = FXCollections.observableArrayList();
    public void receiveData(ArrayList<String> data) {
        for (String files : data
        ) {
            String[] file= files.split("\\.");
            String filename = file[0];
            String fileextension = file[1];
            MyColumn column = new MyColumn(filename, fileextension);
            tableData.add(column);
        }
        tableFile.setItems(tableData);
        columnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFileName()));
        columnExtension.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFileExtension()));

    }
    @FXML
    public void ViewclientList()
    {

        ObservableList<ClientTableColumn> tableData = FXCollections.observableArrayList();
        for (Account account : listAccount
        ) {
            ClientTableColumn account1 = new ClientTableColumn(account.getUsername(),account.getSocket().getInetAddress().toString(),String.valueOf(account.getSocket().getPort()) ,String.valueOf(account.getSocket().getLocalPort()) );
            tableData.add(account1);
        }
        tableClient.setItems(tableData);
        columNameCLient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientName()));
        columnSocketCLient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientSocket().toString()));
        columnSocketPort.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientSocketPort().toString()));
        columnSocketPort2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientSocketPort2().toString()));
    }
    private ArrayList<FilesAndIp> listRequestAccept = new ArrayList<>();
    private ObservableList<ClientRequestColumn> selectedItems = FXCollections.observableArrayList();
    @FXML
    public void viewclientRequest()
    {
        ObservableList<ClientRequestColumn> tableData = FXCollections.observableArrayList();
        for (FilesAndIp files : listRequest
        ) {
            Button tempButton = new Button("Xem");
            tempButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String nameFiles = "";
                    for (String name: files.getListFileName()
                         ) {
                        nameFiles+= name+"\n";
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Danh sách được yêu cầu");
                    alert.setContentText(nameFiles);
                    alert.showAndWait();
                }
            });

            ClientRequestColumn column = new ClientRequestColumn(files.getUsername(),files.getIpClient(),files.getListFileName(),tempButton);
            tableData.add(column);
        }
        tableListRequest.setItems(tableData);
        columnNameCLientRequest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        columnIPCLientRequest.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIP()));
        columnViewCLientRequest.setCellValueFactory(cellData -> {
            Button button = cellData.getValue().getButtonView();
            return new SimpleObjectProperty<>(button);
        });

        columnAcceptCLientRequest.setCellFactory(CheckBoxTableCell.forTableColumn(columnAcceptCLientRequest));
        columnAcceptCLientRequest.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isSelected()));

        columnAcceptCLientRequest.setCellFactory(CheckBoxTableCell.forTableColumn(columnAcceptCLientRequest));
        columnAcceptCLientRequest.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        columnAcceptCLientRequest.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ClientRequestColumn, Boolean> call(TableColumn<ClientRequestColumn, Boolean> param) {
                return new TableCell<>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        setGraphic(checkBox);
                        setEditable(true);
                        checkBox.setOnAction(event -> {
                            ClientRequestColumn rowData = getTableRow().getItem();
                            if (rowData != null) {
                                rowData.setSelected(checkBox.isSelected());
                                FilesAndIp filesAndIp = new FilesAndIp(rowData.getView(),rowData.getIP(),rowData.getName());
                                if (checkBox.isSelected()) {
                                    listRequestAccept.add(filesAndIp);
                                    selectedItems.add(rowData);
                                } else {
                                    listRequestAccept.remove(filesAndIp);
                                    selectedItems.remove(rowData);
                                }
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            checkBox.setSelected(item);
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });
    }
    @FXML
    private void addFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String[] fileName = selectedFile.getName().split("\\.");
            MyColumn column = new MyColumn(fileName[0],fileName[1]);
            tableData.add(column);
            File destination = new File("Data\\" + fileName[0]+"."+fileName[1]);
            listFile.add(fileName[0]+"."+fileName[1]);
            try {
                java.nio.file.Files.copy(selectedFile.toPath(), destination.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    @FXML
    private void deleteFile()
    {
        ObservableList<MyColumn> selectedItems = tableFile.getSelectionModel().getSelectedItems();
        String name = "";
        String extension = "";
        for (MyColumn item : selectedItems) {
            name = item.getFileName();
            extension = item.getFileExtension();
        }
        File fileToDelete = new File("Data\\" + name+"."+extension);
        if (fileToDelete.exists()) {
            try {
                Files.delete(fileToDelete.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        tableData.removeAll(selectedItems);
    }

    @FXML
    private void synchronousAll() throws IOException {
        if(listAccount.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Chưa có người dùng nào kết nối tới");
        }
        else{
            sendFile(listFile);
        }

    }

    private  void sendFile(ArrayList<String> filesToSend) throws IOException {
        filesToSend.add("End.txt");
        Socket socket = serverSocket1.accept();
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        objOut.writeInt(filesToSend.size());
        int len=0;
        while (len < filesToSend.size())
        {
            objOut.writeObject(filesToSend.get(len));
            objOut.flush();
            try (FileInputStream fis = new FileInputStream(new File("Data\\"+filesToSend.get(len)))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    objOut.write(buffer, 0, bytesRead);
                }
            }
            len++;
        }

    }
    @FXML
    private void acceptRequest() throws IOException {
        for (FilesAndIp listFileOfAPerson: listRequestAccept
             ) {
                sendFile(listFileOfAPerson.getListFileName());

        }
        listRequest.removeAll(listRequestAccept);
        tableListRequest.getItems().removeAll(selectedItems);

        selectedItems.clear();
        listRequestAccept.clear();
    }
    @FXML
    private void acceptAll() throws IOException {
        for (FilesAndIp listFileOfAPerson: listRequest
        ) {
            sendFile(listFileOfAPerson.getListFileName());
        }
        tableListRequest.getItems().clear();
        listRequest.clear();
        selectedItems.clear();
        listRequest.clear();
    }
    @FXML
    private void cancelAll()
    {
        tableListRequest.getItems().clear();
        listRequest.clear();
        selectedItems.clear();
        listRequest.clear();
    }
    @FXML
    private void buttonSettTime()  {
        try {
            FXMLLoader loaderTime = new FXMLLoader(getClass().getResource("SetTime.fxml"));
            Parent rootTime = loaderTime.load();

            SetTimeController controller2 = loaderTime.getController();
            controller2.getTime();
            controller2.setup();
            controller2.ReiceiVeTime();

            Stage stageTime = new Stage();
            stageTime.setScene(new Scene(rootTime));
            stageTime.show();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
class ClientHandler implements Runnable {
    private Socket socket;
    private static ArrayList<String> listFile = new ArrayList<>();
    private ArrayList<FilesAndIp> filesAndIpArrayList = new ArrayList<>();
    private List<Account> listAccount = new ArrayList<>();
    private TextField numberAccountConnect;
    private ArrayList<String> listAllFile = new ArrayList<>();
    private ServerSocket serverSocket1;
    private ArrayList<Account> listAccountWithoutThisSocket = new ArrayList<>();
    private Boolean IsNewList = true;
    private ArrayList<ObjectOutputStream> objectOutputStreamArrayList = new ArrayList<>();
    public void setData(ArrayList<String> listAllFile,ServerSocket serverSocket1,ArrayList<ObjectOutputStream> objectOutputStreamArrayList)
    {
        this.objectOutputStreamArrayList =objectOutputStreamArrayList;
        this.serverSocket1 = serverSocket1;
        this.listAllFile = listAllFile;
    }

    public ClientHandler(Socket socket, ArrayList<String> listFile,ArrayList<FilesAndIp> filesAndIpArrayList,List<Account> listAccount,TextField numberAccountConnect) {
        this.socket = socket;
        this.listFile = listFile;
        this.listAccount = listAccount;
        this.filesAndIpArrayList = filesAndIpArrayList;
        this.numberAccountConnect = numberAccountConnect;
    }
    public void Synchnorous() throws SQLException, ClassNotFoundException, IOException {

        Thread thread = new Thread(()->{
            try {
                Date nextTime = null;
                Class.forName("com.mysql.jdbc.Driver");
                String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
                String USER_NAME = "root";
                String PASSWORD = "";
                Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                Statement stmt = conn.createStatement();
                String queryTime = "select * from timetosynchnorou ";
                ResultSet rsTime = stmt.executeQuery(queryTime);

                while (rsTime.next()) {
                    nextTime = rsTime.getDate("NextTime");
                }

                LocalDate currentDate = LocalDate.now();
                Date date = Date.valueOf(currentDate);
                if (date.equals(nextTime))
                {
                    sendFile(listAllFile);
                }
            } catch (ClassNotFoundException | SQLException | IOException e) {
            }
        });
        thread.start();
    }


    private  void sendFile(ArrayList<String> filesToSend) throws IOException {
        Socket socket = serverSocket1.accept();
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        objOut.writeInt(filesToSend.size());
        int len=0;
        while (len < filesToSend.size())
        {
            objOut.writeObject(filesToSend.get(len));
            objOut.flush();
            try (FileInputStream fis = new FileInputStream(new File("Data\\"+filesToSend.get(len)))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    objOut.write(buffer, 0, bytesRead);
                }
            }
            len++;
        }
    }


    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            while (true)
            {

                String Action = in.readUTF();
                System.out.println(Action);
                if (Action.equals("LOGIN")) {
                    String username = "";
                    ArrayList<String> listFileClient = new ArrayList<>();
                    Boolean login = false;
                    while (login == false) {
                        try {

                            username = in.readUTF();
                            String password = in.readUTF();
                            listFileClient = (ArrayList<String>) objectIn.readObject();
                            Boolean checkLogined = false;
                            for (Account account: listAccount
                            ) {
                                if(account.getUsername().equals(username))
                                {
                                    checkLogined = true;
                                    break;
                                }
                            }
                            if (checkLogined)
                            {
                                out.writeUTF("logined");
                            }
                            else {
                                out.writeUTF("not logined");


                                Class.forName("com.mysql.jdbc.Driver");
                                String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
                                String USER_NAME = "root";
                                String PASSWORD = "";
                                Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                                Statement stmt = conn.createStatement();

                                String query = "select * from Account where Username ='" + username + "' and Password ='" + password + "'";
                                ResultSet rs = stmt.executeQuery(query);
                                if (rs.next()) {
                                    out.writeUTF("Ok");
                                    login = true;
                                } else {
                                    out.writeUTF("No");
                                }
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Boolean key = true;
                    for (Account account : listAccount
                    ) {
                        if (username.equals(account.getUsername())) {
                            key = false;
                        }
                    }

                    Account accountCurent = new Account(username, socket, listFileClient, objectOut, objectIn);
                    if (key) {
                        Integer count = Integer.parseInt(numberAccountConnect.getText());
                        listAccount.add(accountCurent);
                        objectOutputStreamArrayList.add(objectOut);
                        this.numberAccountConnect.setText(String.valueOf(count + 1));
                        Synchnorous();
                    }
                    objectOut.writeObject(listFile);
                    try {
                        for (ObjectOutputStream objectOutputStream : objectOutputStreamArrayList
                        ) {
                            for (Account account : listAccount
                            ) {
                                objectOutputStream.writeUTF("ACCOUNT");
                                objectOutputStream.writeObject(account);
                            }
                        }

                        while (true) {
                            String action = in.readUTF();
                            System.out.println(action);
                            if (action.equals("PVS")) {
                                FilesAndIp receivedFilesAndIp = null;
                                receivedFilesAndIp = (FilesAndIp) objectIn.readObject();
                                this.filesAndIpArrayList.add(receivedFilesAndIp);
                            } else if (action.equals("PVP")) {
                                ClientRequest clientRequest = (ClientRequest) objectIn.readObject();
                                String usernameReceive = clientRequest.getAccountReceive();
                                String usernameRequest = clientRequest.getAccountRequest();
                                Boolean IsOnline = false;
                                for (Account account : listAccount
                                ) {
                                    if (usernameReceive.equals(account.getUsername())) {
                                        IsOnline = true;
                                        ObjectOutputStream dataOutputStream = account.getObjectOutputStream();
                                        dataOutputStream.writeUTF("REQUEST");
                                        dataOutputStream.writeObject(clientRequest);
                                    }
                                }
                                if (IsOnline == false)
                                {
                                    System.out.println("NotOnline");
                                    for (Account account : listAccount
                                    ) {
                                        if (usernameRequest.equals(account.getUsername())) {
                                            System.out.println(account.getUsername());
                                            ObjectOutputStream dataOutputStream = account.getObjectOutputStream();
                                            dataOutputStream.writeUTF("NotOnline");
                                        }
                                    }
                                }
                            } else if (action.equals("RESPOND")) {
                                String usernameRequest = in.readUTF();
                                ArrayList<String> filesToSend = new ArrayList<>();
                                ObjectOutputStream objectOutputStream = null;
                                for (Account account : listAccount
                                ) {
                                    if (usernameRequest.equals(account.getUsername())) {

                                        objectOutputStream = account.getObjectOutputStream();
                                    }
                                }

                                int numFiles = objectIn.readInt();
                                for (int i = 0; i < numFiles; i++) {

                                    String fileName = objectIn.readObject().toString();
                                    filesToSend.add(fileName);
                                    filesToSend.add("End.txt");
                                    try (FileOutputStream fos = new FileOutputStream("DataTemp\\" + fileName)) {
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;
                                        while ((bytesRead = objectIn.read(buffer)) != -1) {
                                            fos.write(buffer, 0, bytesRead);
                                        }
                                    }

                                    if (i == numFiles - 2) {
                                        objectOutputStream.writeUTF("SENDRESPOND");
                                        objectOutputStream.writeInt(filesToSend.size());
                                        int len = 0;
                                        while (len < filesToSend.size()) {
                                            objectOutputStream.writeObject(filesToSend.get(len));
                                            objectOutputStream.flush();
                                            try (FileInputStream fis = new FileInputStream(new File("DataTemp\\" + filesToSend.get(len)))) {
                                                byte[] buffer = new byte[1024];
                                                int bytesRead;
                                                while ((bytesRead = fis.read(buffer)) != -1) {
                                                    objectOutputStream.write(buffer, 0, bytesRead);
                                                }
                                            }
                                            len++;
                                        }
                                        for (String file : filesToSend
                                        ) {
                                            File fileToDelete = new File("DataTemp\\" + file);
                                            if (fileToDelete.exists()) {
                                                try {
                                                    Files.delete(fileToDelete.toPath());
                                                } catch (IOException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    } catch (IOException e) {

                    } finally {
                        listAccount.remove(accountCurent);
                        objectOutputStreamArrayList.remove(objectOut);
                        Integer count = Integer.parseInt(numberAccountConnect.getText());
                        this.numberAccountConnect.setText(String.valueOf(count - 1));
                    }
                } else if (Action.equals("SIGNUP")) {
                    String username = in.readUTF();
                    String password = in.readUTF();
                    String email = in.readUTF();

                    Class.forName("com.mysql.jdbc.Driver");
                    String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
                    String USER_NAME = "root";
                    String PASSWORD = "";
                    Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                    Statement stmt = conn.createStatement();

                    String query = "select * from account where Username ='" + username + "' or Email ='" + email + "' ";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        out.writeUTF("No");
                    } else {
                        String queryAdd = "INSERT INTO `account`(`Username`, `Password`, `Email`, `Status`) VALUES ('" + username + "','" + password + "','" + email + "','1')";
                        int result = stmt.executeUpdate(queryAdd);
                        out.writeUTF("Ok");
                    }
                }
                else if(Action.equals("FORGET"))
                {
                    String email = in.readUTF();
                    Class.forName("com.mysql.jdbc.Driver");
                    String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
                    String USER_NAME = "root";
                    String PASSWORD = "";
                    Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                    Statement stmt = conn.createStatement();
                    String query = "select * from Account where Email ='" + email + "' ";
                    ResultSet rs = stmt.executeQuery(query);

                    if(rs.next()) {
                        out.writeUTF("Ok");
                        String password = rs.getString("Password");
                        out.writeUTF(password);
                    } else {
                        out.writeUTF("No");
                    }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
class ClientRequestColumn {
    private  String Name;
    private  String IP;
    private Button buttonView;
    private ArrayList<String> View;
    private final SimpleBooleanProperty Accept;

    public ClientRequestColumn(String clientName, String clientIP, ArrayList<String> view,Button buttonView) {
        this.Name = clientName;
        this.IP = clientIP;
        this.buttonView = buttonView;
        this.View = view;
        this.Accept = new SimpleBooleanProperty(false);
    }

    public Button getButtonView() {
        return buttonView;
    }

    public void setButtonView(Button buttonView) {
        this.buttonView = buttonView;
    }

    public String getName() {
        return Name;
    }

    public void setClientName(String clientName) {
        this.Name = clientName;
    }

    public String getIP() {
        return IP;
    }

    public void setClientIP(String clientIP) {
        this.IP = clientIP;
    }

    public ArrayList<String> getView() {
        return View;
    }

    public void setView(ArrayList<String> view) {
        View = view;
    }

    public boolean isSelected() {
        return Accept.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return Accept;
    }

    public void setSelected(boolean selected) {
        this.Accept.set(selected);
    }
}
class ClientTableColumn  {
    private  String clientName;
    private  String clientSocket;
    private  String clientSocketPort;
    private  String clientSocketPort2;

    public ClientTableColumn(String username, String address, String port,String localport) {
        this.clientName = username;
        this.clientSocket = address;
        this.clientSocketPort = port;
        this.clientSocketPort2 = localport;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(String clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getClientSocketPort() {
        return clientSocketPort;
    }

    public void setClientSocketPort(String clientSocketPort) {
        this.clientSocketPort = clientSocketPort;
    }

    public String getClientSocketPort2() {
        return clientSocketPort2;
    }

    public void setClientSocketPort2(String clientSocketPort2) {
        this.clientSocketPort2 = clientSocketPort2;
    }
}

class MyColumn {
    private final StringProperty fileName;
    private final StringProperty fileExtension;

    public MyColumn(String fileName, String fileExtension) {
        this.fileName = new SimpleStringProperty(fileName);
        this.fileExtension = new SimpleStringProperty(fileExtension);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public String getFileExtension() {
        return fileExtension.get();
    }

    public StringProperty fileExtensionProperty() {
        return fileExtension;
    }

}


