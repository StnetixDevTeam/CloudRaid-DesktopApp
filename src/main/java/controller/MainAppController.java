package controller;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Anton on 25.06.2016.
 */
public class MainAppController {
    MainApp app;
    Stage settingsStage;

    @FXML
    private AnchorPane browserContainer;


    @FXML
    void showSettingsWindow(){
        System.out.println("show");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/settings.fxml"));
            AnchorPane pane = loader.load();


            settingsStage = new Stage();
            ((SettingsController)loader.getController()).setSettingsStage(settingsStage);
            ((SettingsController)loader.getController()).setDataManager(app.getDataManager());
            settingsStage.setTitle("Settings");
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(pane, 700, 700);
            settingsStage.setScene(scene);
            settingsStage.showAndWait();



        } catch (IOException e){

        }
    }

    public AnchorPane getBrowserContainer(){
        return browserContainer;
    }
    public void setMainApp(MainApp app){
        this.app = app;
    }


}
