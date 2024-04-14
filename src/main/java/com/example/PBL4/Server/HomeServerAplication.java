package com.example.PBL4.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeServerAplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeServerController.class.getResource("HomeServer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 814, 632);
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();

    }
}
