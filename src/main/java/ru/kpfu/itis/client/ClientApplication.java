package ru.kpfu.itis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application {

    private static final String FXML_FILE_NAME = "/fxml/main.fxml";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));

        Scene scene = new Scene(root);


        primaryStage.setTitle("Кто ищет, тот всегда найдёт");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
