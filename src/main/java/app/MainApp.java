package app;

import browserApp.BrowserApp;
import controller.MainAppController;
import events.ChangeEvent;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DAOFactory;
import model.DAOFileItem;
import model.EFSItem;
import model.FileItem;
import synchronizeService.SyncService;
import util.AppSettings;
import util.EntityUtil;

/**
 *Start point
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private DAOFileItem dataManager;
    private Stage primaryStage;
    private AppSettings settings;

    @Override
    public void start(Stage primaryStage) {

        settings = AppSettings.getInstance();
        EntityManager entityManager = null;
        try {
            entityManager = EntityUtil.setUp().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DAOFactory daoFactory = DAOFactory.getInstance(entityManager);
        dataManager = daoFactory.getDAOFileItem();
        this.primaryStage = primaryStage;

        //dataManager = new DAOFileItemImpl(entityManager);

        BrowserApp browserApp = null;
        try {
            browserApp = new BrowserApp(dataManager, primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<FileItem> items = browserApp.getItems();
        browserApp.addChangeListener(this::fileBrowserChangeListener);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/main.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //Test
        SyncService syncService = new SyncService(dataManager, Paths.get(AppSettings.getInstance().getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH)));
        try {
            syncService.createDirectoryStructureFromEFS();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileBrowserChangeListener(ChangeEvent e){
        ///TEST
        System.out.println(e);
    }

    public DAOFileItem getDataManager() {
        return dataManager;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void onDeleteEFSFile(EFSItem i){
        FileItem parent = i.getParent();
        if (parent.isSync()){
            Path syncFolder = Paths.get(settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH));

        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
