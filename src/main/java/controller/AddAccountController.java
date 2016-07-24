package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.CloudSettings;
import util.CloudSettingsManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Anton on 03.07.2016.
 */
public class AddAccountController implements Initializable{
    private CloudSettingsManager manager;
    private CloudSettingsManager.Cloud item;
    private CloudSettings settings;
    private Stage stage;
    @FXML
    private ChoiceBox<?> cloudsList;

    @FXML
    private Label availableSpaceLable;

    @FXML
    private Label totalSpaceLable;

    @FXML
    private Label cloudSpaceLable;

    @FXML
    private Slider slider;

    @FXML
    private TextField name;

    @FXML
    private GridPane spaceSettings;

    @FXML
    GridPane loginGrid;

    @FXML
    private Button connectBtn;

    @FXML
    private Button saveBtn;

    @FXML
    void connectHandler(ActionEvent event) {
        spaceSettings.setDisable(false);
        saveBtn.setDisable(false);
        generateSomeData();
    }

    @FXML
    void saveSettingsHandler(ActionEvent event) {
        settings.setName(name.getText());
        manager.addCloudAccount(settings);
        stage.close();
    }

    @FXML
    void cancelHandler(){
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init");

        cloudsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                item = (CloudSettingsManager.Cloud)newValue;
                loginGrid.setDisable(false);
                connectBtn.setDisable(false);

            }
        });

    }

    public void init(CloudSettingsManager manager, Stage stage){
        this.stage = stage;
        this.manager = manager;
        cloudsList.setItems(FXCollections.observableArrayList(new ArrayList(manager.getClouds().values())));
    }

    private void generateSomeData(){
        long total = 10;
        long available = 8;
        slider.setMax(available);
        slider.setMin(0);
        cloudSpaceLable.setText(String.valueOf(slider.getValue()));
        settings = new CloudSettings(name.getText(), total, 2, (long) slider.getValue(), item);
        slider.setOnMouseReleased(event -> {
            settings.setCloudRaidSpace((long)slider.getValue());
            cloudSpaceLable.setText(String.valueOf(slider.getValue()));
        });

    }
}
