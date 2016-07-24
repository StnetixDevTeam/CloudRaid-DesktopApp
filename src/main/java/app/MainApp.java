package app;

import browserApp.BrowserApp;
import controller.MainAppController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DAOFactory;
import model.DAOFileItem;
import model.FileItem;
import util.EntityUtil;

import javax.persistence.EntityManager;

public class MainApp extends Application {

    private ObservableList<FileItem> items;
    private DAOFileItem dataManager;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{


        EntityManager entityManager = EntityUtil.setUp().createEntityManager();
        DAOFactory daoFactory = DAOFactory.getInstance(entityManager);
        dataManager = daoFactory.getDAOFileItem();
        this.primaryStage = primaryStage;
        items = FXCollections.observableArrayList();

        //dataManager = new DAOFileItemImpl(entityManager);

        BrowserApp browserApp = new BrowserApp(dataManager, primaryStage);
        items = browserApp.getItems();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/main.fxml"));

        Parent root = loader.load();
        MainAppController mainAppController = loader.getController();
        mainAppController.setMainApp(this);
        AnchorPane browserContainer = mainAppController.getBrowserContainer();
        browserContainer.getChildren().add(browserApp.pane);
        browserApp.pane.setFillWidth(true);
        primaryStage.setTitle("Hello World");


        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();


        dataManager.getContent(dataManager.getRoot());

        primaryStage.setOnCloseRequest(event -> {
            try {
                dataManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DAOFileItem getDataManager(){
        return dataManager;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
