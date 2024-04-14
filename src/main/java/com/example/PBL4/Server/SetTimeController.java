package com.example.PBL4.Server;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class SetTimeController {
    @FXML
    private DatePicker NextTimeView;
    @FXML
    private DatePicker LastTimeView;
    @FXML
    private TextField TimeView;
    private Integer numberTime;
    private Date NextTime;
    private Date LastTime;
    private Connection connection;
    public void ReceiveConnection(Connection connection)
    {
        this.connection = connection;
    }
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
        String USER_NAME = "root";
        String PASSWORD = "";
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        return conn;
    }
    public void getTime() throws ClassNotFoundException, SQLException {
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
    public void ReiceiVeTime() {

        LocalDate localNextTime = NextTime.toLocalDate();
        this.NextTimeView.setValue(localNextTime);


        LocalDate localLastTime = LastTime.toLocalDate();
        this.LastTimeView.setValue(localLastTime);

        this.TimeView.setText(String.valueOf(numberTime));
    }
    public void setup() {

        try
        {
            LocalDate localNextTime = NextTime.toLocalDate();
            LocalDate localLastTime = LastTime.toLocalDate();
            System.out.println(localNextTime);

            LocalDate currentDate = LocalDate.now();
            Date date = Date.valueOf(currentDate);
            if (localNextTime.plusDays(1).equals(currentDate))
            {
                System.out.println("Hello");
                Class.forName("com.mysql.jdbc.Driver");
                String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
                String USER_NAME = "root";
                String PASSWORD = "";
                Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                Statement stmt = conn.createStatement();

                String query = "UPDATE `timetosynchnorou` SET `NextTime`='"+localNextTime.plusDays(numberTime)+"', `LastTime`='"+this.NextTime+"' WHERE `LastTime`='"+this.LastTime+"' ";
                Integer rs = stmt.executeUpdate(query);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    @FXML
    public void ChangeTime()
    {
        Integer newTime = Integer.parseInt(TimeView.getText());
        LocalDate localLastTime = LastTime.toLocalDate();
        LocalDate futureDate = localLastTime.plusDays(newTime);
        this.NextTimeView.setValue(futureDate);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String DB_URL = "jdbc:mysql://localhost:3306/pbl4";
            String USER_NAME = "root";
            String PASSWORD = "";
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            Statement stmt = conn.createStatement();

            String query = "UPDATE `timetosynchnorou` SET `NextTime`='"+futureDate+"',`Time`='"+newTime+"' WHERE `LastTime`='"+this.LastTime+"'";
            System.out.println(query);
            Integer rs = stmt.executeUpdate(query);
            if (rs > 0) {
                JOptionPane.showMessageDialog(null, "Thay đổi thành công");
            } else {
                JOptionPane.showMessageDialog(null, "Thay đổi thất bại");
            }
        }
        catch (Exception ex)
        {

        }



    }


}
