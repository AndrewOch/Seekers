package ru.kpfu.itis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.controller.MainController;

public class ClientApplication extends Application {

    private static final String FXML_FILE_NAME = "main.fxml";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));


       /* AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinSize(500, 500);
        Button button =  new Button();
        button.setText("Кнопка");
        anchorPane.getChildren().add(button);*/


        Scene scene = new Scene(root);


        primaryStage.setTitle("Кто ищет, тот всегда найдёт");
        primaryStage.setScene(scene);
        //MainController mainController = fxmlLoader.getController();
        //scene.setOnKeyPressed(mainController.getPlayerControlEvent());

        primaryStage.show();
    }
}
