package browserApp;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DAOFileItem;
import model.FileItem;

import java.io.IOException;

/**
 * Created by Anton on 17.06.2016.
 */
public class BrowserApp {
    public MainController controller;
    public AnchorPane container;
    public VBox pane;
    private ObservableList<FileItem> items;
    private Stage stage;

    public BrowserApp(DAOFileItem dataAdapter, Stage stage) throws IOException {

        this.stage = stage;
        this.items = FXCollections.observableArrayList();
        dataAdapter.setContent(items);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/fileBrowser.fxml"));
        container = loader.load();
        controller = loader.getController();
        pane = controller.getBrowserContainer();

        controller.init(items, dataAdapter);
        controller.setStage(stage);
    }

    public ObservableList<FileItem> getItems(){
        return items;
    }
}
