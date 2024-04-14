package com.example.PBL4.Server;

import com.example.PBL4.FilesAndIp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

public class Temp {
    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame();
        jFrame.setSize(1465,780);
        Color backgroundColorFrame= new Color(162, 255, 222);
        jFrame.setBackground(backgroundColorFrame);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(null);


        JPanel jpanelTable = new JPanel();
        jpanelTable.setBounds(0,0,385,780);
        Color backgroundColorTable = new Color(3, 149, 255);
        jpanelTable.setBackground(backgroundColorTable);
        jpanelTable.setLayout(null);
            JLabel labelHome = new JLabel("HOME");
            labelHome.setBounds(45,47,287,72);
            labelHome.setHorizontalAlignment(SwingConstants.CENTER);
            labelHome.setVerticalAlignment(SwingConstants.CENTER);
            Font font = new Font("Gravitas One", Font.PLAIN, 32);
            labelHome.setFont(font);
            labelHome.setForeground(Color.WHITE);



            JButton button1 = new JButton("Danh sách client");
            button1.setBounds(0,141,385,100);
            Color backgroundColorButton1 = new Color(249, 213, 213);
            button1.setBackground(backgroundColorButton1);
            Font fontButton = new Font("Fredoka One", Font.PLAIN, 20);
            button1.setFont(fontButton);

            JButton button2 = new JButton("Danh sách file");
            button2.setBounds(0,273,385,100);
            Color backgroundColorButton2 = new Color(192, 254, 246);
            button2.setBackground(backgroundColorButton2);
            button2.setFont(fontButton);

            JButton button3 = new JButton("Yêu cầu");
            button3.setBounds(0,405,385,100);
            Color backgroundColorButton3 = new Color(254, 214, 255);
            button3.setBackground(backgroundColorButton3);
            button3.setFont(fontButton);

            JButton button4 = new JButton("Bắt đầu");
            button4.setBounds(0,650,385,100);
            Color backgroundColorButton4 = new Color(140, 255, 172);
            button4.setBackground(backgroundColorButton4);
            button4.setFont(fontButton);

            jpanelTable.add(labelHome);
            jpanelTable.add(button1);
            jpanelTable.add(button2);
            jpanelTable.add(button3);
            jpanelTable.add(button4);
        JPanel jPanelContent = new JPanel();
        jPanelContent.setLayout(null);
        jPanelContent.setBounds(401,19,1047,710);
        jPanelContent.setPreferredSize(new Dimension(1000, 10000));
        Color backgroundColorContent = new Color(247, 247, 219);
        jPanelContent.setBackground(backgroundColorContent);
        JScrollPane scrollPane = new JScrollPane(jPanelContent);
        scrollPane.setBounds(401,19,1030,710);
        setContent content = new setContent(jPanelContent);
        content.process(0);


        ArrayList<Socket> listSocket = new ArrayList<>();
        ArrayList<FilesAndIp> listRequest = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(8888);
        ServerSocket serverSocket1 = new ServerSocket(8889);

        File directory = new File("Data\\");
        File[] files = directory.listFiles();
        ArrayList<String> listFile = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                listFile.add(file.getName());
            }
        }
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Integer count = 0;
                            while (true) {
                                Socket socket = serverSocket.accept();
                                listSocket.add(socket);
                                ClientHandlerTemp clientHandler = new ClientHandlerTemp(socket,listFile,jFrame,count,serverSocket1,listRequest);
                                new Thread(clientHandler).start();
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                serverThread.start();
            }
        });


        labelHome.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("label 0");
                setContent content = new setContent(jPanelContent);
                content.process(0);
                jFrame.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContent content = new setContent(jPanelContent);
                content.process(2);
                jFrame.repaint();
            }
        });

        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setContent content = new setContent(jPanelContent);
                content.setListSocket(listSocket);
                content.process(1);
                jFrame.repaint();
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (FilesAndIp filesAndIp:listRequest
                ) {
                    System.out.println(filesAndIp.toString());
                }

                setContent setContent = new setContent(jPanelContent);
                setContent.setServerSocket(serverSocket1);
                try {
                    setContent.setListFileAndIp(listRequest);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                jFrame.repaint();
            }
        });
        jFrame.add(scrollPane);
        jFrame.add(jpanelTable);
       // jFrame.add(jPanelContent);
        jFrame.setVisible(true);

    }
}
class setContent{
    private JPanel jPanel;
    public ServerSocket serverSocket;
    private ArrayList<Socket> listSocket = new ArrayList<>();
    private ArrayList<FilesAndIp> filesAndIpArrayList = new ArrayList<>();
    public void setListSocket(ArrayList<Socket> listSocket)
    {
        this.listSocket.clear();
        this.listSocket = listSocket;
    }
    public  void setServerSocket(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }
    public void setListFileAndIp(ArrayList<FilesAndIp> filesAndIpArrayList) throws IOException {
        this.filesAndIpArrayList.clear();
        this.filesAndIpArrayList = filesAndIpArrayList;
        setContentKey3(this.filesAndIpArrayList);
    }
    public setContent(JPanel jPanel)
    {
        this.jPanel = jPanel;
    }
    public void process(Integer key)
    {
        if (key==0)
        {
            setContentKey0();
        }
        else if (key==1) {
            setContentKey1();
        }
        else if (key==2) {
            setContentKey2();
        }

    }
    public void setContentKey0()
    {
        JLabel labelTitle = new JLabel("PBL 4 : Đồ án hệ điều hành và mạng máy tính");
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setVerticalAlignment(SwingConstants.CENTER);
        labelTitle.setBounds(10,30,1028,72);
        Font fontButton = new Font("Suez One", Font.PLAIN, 28);
        labelTitle.setFont(fontButton);

        JLabel nameSV1 = new JLabel("Họ tên : Nguyễn Văn Nhật\n Mssv : 102210120\n Lớp : 21T_DT2");
        nameSV1.setBounds(77, 90, 905, 32);
        JLabel nameSV2 = new JLabel("Họ tên : Trương Ngọc Sơn\n Mssv : 102210125\n Lớp : 21T_DT2");
        nameSV2.setBounds(77, 129, 905, 32);
        Font fontName = new Font("Strong", Font.PLAIN, 20);
        nameSV2.setFont(fontName);
        nameSV1.setFont(fontName);
        JLabel label = new JLabel("Nội dung đề tài ");
        label.setBounds(77,196,428,42);
        label.setFont(fontName);

        JPanel content = new JPanel();
        content.setBounds(77,254,880,407);
        content.setBackground(Color.white);

        this.jPanel.removeAll();
        this.jPanel.add(content);
        this.jPanel.add(label);
        this.jPanel.add(nameSV1);
        this.jPanel.add(nameSV2);
        this.jPanel.add(labelTitle);
    }
    public void setContentKey1()
    {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("IP");
        tableModel.addColumn("PORT");
        tableModel.addColumn("LOCALPORT");
        JTable fileTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(fileTable);
        scrollPane.setBounds(9,31,1000,600);
        for (Socket socket: listSocket
             ) {
            String ip = socket.getInetAddress().getHostAddress();
            int port = socket.getPort();
            int localPort = socket.getLocalPort();

            tableModel.addRow(new Object[]{ip, port, localPort});
        }
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            fileTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new CustomHeaderRenderer());
        }
        JTableHeader tableHeader = fileTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 60));
        this.jPanel.removeAll();
        this.jPanel.add(scrollPane);
    }
    public void setContentKey2()
    {

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Tên file");
            tableModel.addColumn("Loại file");
            JTable fileTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(fileTable);
            scrollPane.setBounds(9,31,1000,600);
            File directory = new File("Data\\");
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    String[] name = file.getName().split("\\.");
                    tableModel.addRow(new Object[]{name[0],name[1]});
                }
            }
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                fileTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new CustomHeaderRenderer());
            }
            JTableHeader tableHeader = fileTable.getTableHeader();
            tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 60));
            JButton jButton = new JButton("Thêm file");
            jButton.setBounds(9,650,100,40);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String[] fileName = selectedFile.getName().split("\\.");
                        tableModel.addRow(new Object[]{fileName[0],fileName[1]});

                        File destination = new File("Data\\" + fileName[0]+"."+fileName[1]);
                        try {
                            Files.copy(selectedFile.toPath(), destination.toPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

        JButton deleteButton = new JButton("Xóa file");
        deleteButton.setBounds(120, 650, 100, 40);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fileTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String fileName = (String) tableModel.getValueAt(selectedRow, 0)+"."+(String) tableModel.getValueAt(selectedRow, 1);
                    tableModel.removeRow(selectedRow);

                    File fileToDelete = new File("Data\\" + fileName);
                    if (fileToDelete.exists()) {
                        try {
                            Files.delete(fileToDelete.toPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        JButton renameButton = new JButton("Đổi tên");
        renameButton.setBounds(231, 650, 100, 40);
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fileTable.getSelectedRow();
                if (selectedRow != -1) {
                    String newName = (String) tableModel.getValueAt(selectedRow, 0);
                    String extension = (String) tableModel.getValueAt(selectedRow, 1);
                    String oldFileName = newName + "." + extension;
                    File directory = new File("Data\\");
                    File oldFile = new File(directory, oldFileName);

                    if (oldFile.exists()) {
                        String newFileName = JOptionPane.showInputDialog(null, "Nhập tên mới:", newName);

                        if (newFileName != null) {
                            File newFile = new File(directory, newFileName + "." + extension);
                            if (oldFile.renameTo(newFile)) {
                                tableModel.setValueAt(newFileName, selectedRow, 0);
                            } else {
                                JOptionPane.showMessageDialog(null, "Không thể đổi tên tệp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tệp không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

            this.jPanel.removeAll();
            this.jPanel.add(jButton);
            this.jPanel.add(renameButton);
            this.jPanel.add(scrollPane);
            this.jPanel.add(deleteButton);

    }
    public void setContentKey3(ArrayList<FilesAndIp> filesAndIpArrayList) throws IOException {

        int xLabel = 10;
        int yLabel = 10;
        int widthLabel = 100;
        int heightLabel = 40;
        int i = 0;
        this.jPanel.removeAll();
        int k = 0;

        for (FilesAndIp filesAndIp : filesAndIpArrayList) {
            final FilesAndIp currentFilesAndIp = filesAndIp;

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn(filesAndIp.getIpClient());
            JTable fileTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(fileTable);

            scrollPane.setBounds(xLabel + 50 + k * 400, yLabel + i * 300 + 50, 300, 150);
            for (String filename : filesAndIp.getListFileName()) {
                tableModel.addRow(new Object[]{filename});
            }

            Button buttonAccept = new Button("Đồng bộ");
            buttonAccept.setBounds(xLabel + 50 + k * 400, yLabel + i * 300 + 80 + 150, 150, 40);
            Button buttonDeny = new Button("Từ chối");
            Color buttonDenyColor = new Color(255, 122, 122);
            buttonDeny.setBackground(buttonDenyColor);
            buttonDeny.setBounds(xLabel + 50 + k * 400 + 150, yLabel + i * 300 + 80 + 150, 150, 40);

            buttonAccept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        sendFile(currentFilesAndIp.getListFileName());
                        filesAndIpArrayList.remove(currentFilesAndIp);
                        jPanel.remove(buttonAccept);
                        jPanel.remove(buttonDeny);
                        jPanel.remove(scrollPane);
                        jPanel.revalidate();
                        jPanel.repaint();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            buttonDeny.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filesAndIpArrayList.remove(currentFilesAndIp);
                    jPanel.remove(buttonAccept);
                    jPanel.remove(buttonDeny);
                    jPanel.remove(scrollPane);
                    jPanel.revalidate();
                    jPanel.repaint();
                }
            });

            this.jPanel.add(buttonAccept);
            this.jPanel.add(buttonDeny);
            this.jPanel.add(scrollPane);

            if (k == 0) {
                k = 1;
            } else {
                k = 0;
                i++;
            }
        }
        if(filesAndIpArrayList.size()>0) {
            JButton buttonAll = new JButton("Đồng bộ tất cả");
            JButton buttonCancelAll = new JButton("Từ chối tất cả");

            buttonAll.setBounds(0, 0, 200, 40);
            buttonCancelAll.setBounds(200, 0, 200, 40);

            buttonAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (FilesAndIp filesAndIp : filesAndIpArrayList) {
                        try {
                            sendFile(filesAndIp.getListFileName());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    filesAndIpArrayList.clear();
                    jPanel.removeAll();
                    jPanel.repaint();
                }
            });
            buttonCancelAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filesAndIpArrayList.clear();
                    jPanel.removeAll();
                    jPanel.repaint();
                }
            });

            this.jPanel.add(buttonAll);
            this.jPanel.add(buttonCancelAll);
        }
    }
    private  void sendFile(ArrayList<String> filesToSend) throws IOException {
        Socket socket = serverSocket.accept();
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        objOut.writeInt(filesToSend.size());
        for (String file : filesToSend) {
            objOut.writeObject(file);
            try (FileInputStream fis = new FileInputStream(new File("Data\\"+file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    objOut.write(buffer, 0, bytesRead); // Gửi dữ liệu tệp
                }
            }
        }
        System.out.println("Đã gửi " + filesToSend.size() + " tệp đến " + socket.getInetAddress());
    }

}
class CustomHeaderRenderer extends DefaultTableCellRenderer {
    public CustomHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(new Color(22, 236, 124)); // Màu nền đỏ cho tiêu đề cột
        setForeground(Color.WHITE); // Màu chữ trắng cho tiêu đề cột
    }
}
class ClientHandlerTemp implements Runnable {
    private Socket socket;
    private ServerSocket serverSocket;
    private JFrame jFrame;
    private Integer count;
    private static ArrayList<String> listFile = new ArrayList<>();
    private ArrayList<FilesAndIp> filesAndIpArrayList = new ArrayList<>();
    public ClientHandlerTemp(Socket socket, ArrayList<String> listFile, JFrame jFrame, Integer count,ServerSocket serverSocket,ArrayList<FilesAndIp> filesAndIpArrayList) {
        this.socket = socket;
        this.listFile = listFile;
        this.jFrame = jFrame;
        this.serverSocket = serverSocket;
        this.count = count;
        this.filesAndIpArrayList = filesAndIpArrayList;
    }


    @Override
    public void run() {
        try {

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());


            out.writeUTF("Server starting ..");
            System.out.println(in.readUTF());
            objectOut.writeObject(listFile);
            while (true) {
                FilesAndIp receivedFilesAndIp = (FilesAndIp) objectIn.readObject();
                this.filesAndIpArrayList.add(receivedFilesAndIp);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}