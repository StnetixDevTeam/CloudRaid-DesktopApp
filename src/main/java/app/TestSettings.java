package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Anton on 02.07.2016.
 */
public class TestSettings extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/settings.fxml"));

            AnchorPane pane = loader.load();

            primaryStage.setTitle("Settings");
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);

            primaryStage.show();



        } catch (IOException e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
