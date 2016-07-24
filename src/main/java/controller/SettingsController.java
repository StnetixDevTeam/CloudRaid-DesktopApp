package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CloudSettings;
import model.DAOFileItem;
import util.CloudSettingsManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Anton on 02.07.2016.
 */
public class SettingsController implements Initializable{
    CloudSettingsManager settingsMananger = new CloudSettingsManager();
    Stage settingsStage;
    DAOFileItem dataManager;

    @FXML
    AnchorPane accountsTreePane;

    @FXML
    private Slider cloudRaidSpaceSlider;

    @FXML
    private Label totalSpaceLable;

    @FXML
    private Label availableSpaceLable;

    @FXML
    private Label cloudRaidSpaceLable;

    @FXML
    private PieChart chart;

    @FXML
    private Label cloudNameLable;

    @FXML
    private void addAccountHandler(ActionEvent event){
        System.out.println("open dialog");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/addAccountDialog.fxml"));
            AnchorPane pane = loader.load();


            Stage stage = new Stage();
            ((AddAccountController)loader.getController()).init(settingsMananger, stage);
            stage.setTitle("Settings");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(settingsStage);
            Scene scene = new Scene(pane, 300, 350);
            stage.setScene(scene);
            stage.showAndWait();
            initAccountsTreeView();



        } catch (IOException e){

        }
    }

    @FXML
    void openSyncManagerHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/syncManager.fxml"));
            AnchorPane pane = loader.load();


            Stage stage = new Stage();
            ((SyncManagerController)loader.getController()).setDataManager(dataManager);
            ((SyncManagerController)loader.getController()).init();
            stage.setTitle("Sync settings");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(settingsStage);
            Scene scene = new Scene(pane, 500, 600);
            stage.setScene(scene);
            stage.showAndWait();



        } catch (IOException e){

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAccountsTreeView();

    }

    private void initAccountsTreeView(){

        TreeItem<CloudSettings> root = new TreeItem<>(new CloudSettings());
        for (CloudSettings setting:settingsMananger.getCloudAccounts()) {
            TreeItem<CloudSettings> item = new TreeItem<>(setting, new ImageView(setting.getImage()));
            root.getChildren().add(item);
        }
        TreeView<CloudSettings> accountsTree = new TreeView<>(root);
        accountsTree.setShowRoot(false);
        accountsTreePane.getChildren().add(accountsTree);

        accountsTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CloudSettings set = newValue.getValue();

            updateChart(set);
            cloudNameLable.setGraphic(new ImageView(set.getBigImage()));
            cloudNameLable.setText(set.getName());


            totalSpaceLable.setText(String.valueOf(set.getTotalSpace()));
            availableSpaceLable.setText(String.valueOf(set.getAvailableSpace()));
            cloudRaidSpaceLable.setText(String.valueOf(set.getCloudRaidSpace()));
            cloudRaidSpaceSlider.setMax(set.getTotalSpace());
            cloudRaidSpaceSlider.setMin(0);
            cloudRaidSpaceSlider.adjustValue(set.getCloudRaidSpace());
            cloudRaidSpaceSlider.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                cloudRaidSpaceLable.setText(String.valueOf(newValue1.longValue()));
            });
            cloudRaidSpaceSlider.setOnMouseReleased(event -> {
                set.setCloudRaidSpace((long) cloudRaidSpaceSlider.getValue());
                availableSpaceLable.setText(String.valueOf(set.getAvailableSpace()));
                updateChart(set);
            });





        });


    }
    private void updateChart(CloudSettings set){
        PieChart.Data availableData = new PieChart.Data("Available", set.getAvailableSpace());
        PieChart.Data usingData = new PieChart.Data("Using", set.getUsingSpace());
        PieChart.Data totalData = new PieChart.Data("Empty", set.getTotalSpace()-set.getCloudRaidSpace());


        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                availableData, usingData, totalData
        );
        chart.setData(list);

        applyCustomColorSequence(
                list,
                "bisque",
                "red",
                "aqua"
        );
    }
    private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
            i++;
        }
    }
    public void setSettingsStage(Stage stage){
        settingsStage = stage;
    }
    public void setDataManager(DAOFileItem dataManager){
        this.dataManager = dataManager;
    }

}
