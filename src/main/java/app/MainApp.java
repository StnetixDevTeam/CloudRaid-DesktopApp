package app;

import browserApp.BrowserApp;
import controller.MainAppController;
import events.FileBrowserEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DAOFactory;
import model.DAOFileItem;
import newWatchingService.ChangeSyncFolderListener;
import newWatchingService.DirectoryWatchingService;
import newWatchingService.EventsConsumer;
import synchronizeService.ChangeFilesEvent;
import synchronizeService.SyncService;
import util.AppSettings;
import util.EntityUtil;


import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *Start point
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */

public class MainApp extends Application {

    private DAOFileItem dataManager;
    private Stage primaryStage;
    private AppSettings settings;
    private SyncService syncService;
    private DirectoryWatchingService directoryWatchingService;

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
        //ObservableList<FileItem> items = browserApp.getItems();

        //Подписка на события FileBrowser(изменения в EFS)
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
                syncService.stop();
                directoryWatchingService.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //сервис отслеживания изменений на диске в syncFolder
        directoryWatchingService = new DirectoryWatchingService();
        directoryWatchingService.addConsumerEventListener((type, source, target) -> {
            //System.out.println("From File system: "+type);
            switch (type){
                case DELETE:
                    syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.DELETE, source, null));
                    //onDeleteEFSFile(e.getFile());
                    break;
                case CREATE:
                    syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.CREATE, source, null));
                    //onCreateEFSFile(e.getFile());
                    break;
                case RENAME:
                    syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.RENAME, source, null, target.getFileName().toString()));
                    //onRenameEFSFIle(e.getFile(), e.getOldName());
            }
        });
        directoryWatchingService.start();

        //Создаём сервис синхронизации изменений на диске в папке синхронизации с EFS (пока не запущен)
        syncService = new SyncService(dataManager, Paths.get(AppSettings.getInstance().getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH)));
        try {
            syncService.createDirectoryStructureFromEFS();
            new Thread(syncService).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод принимает события из FileBrowser и отправляет их в очередь на SyncService
     * @param e событие FileBrowser
     */
    public void fileBrowserChangeListener(FileBrowserEvent e){
        switch (e.getType()){
            case DELETE:
                syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.DELETE, null, e.getFile()));
                //onDeleteEFSFile(e.getFile());
                break;
            case CREATE:
                syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.CREATE, null, e.getFile()));
                //onCreateEFSFile(e.getFile());
                break;
            case RENAME:
                syncService.addEvent(new ChangeFilesEvent(ChangeFilesEvent.EVENT_TYPES.RENAME, null, e.getFile(), e.getOldName()));
                //onRenameEFSFIle(e.getFile(), e.getOldName());
        }
        //System.out.println(e);
    }

    public DAOFileItem getDataManager() {
        return dataManager;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }


}
