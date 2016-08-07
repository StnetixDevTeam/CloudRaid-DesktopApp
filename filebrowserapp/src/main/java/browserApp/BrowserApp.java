package browserApp;

import controller.MainController;
import events.ChangeEvent;
import events.ChangeEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DAOFileItem;
import model.FileItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class BrowserApp {
    public MainController controller;
    public AnchorPane container;
    public VBox pane;
    private ObservableList<FileItem> items;
    private Stage stage;
    private List<ChangeEventListener> listeners;

    public BrowserApp(DAOFileItem dataAdapter, Stage stage) throws IOException {

        this.stage = stage;
        this.items = FXCollections.observableArrayList();

        listeners = new ArrayList<>();

        dataAdapter.setContent(items);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/fileBrowser.fxml"));
        container = loader.load();
        controller = loader.getController();
        controller.setMainApp(this);
        pane = controller.getBrowserContainer();

        controller.init(items, dataAdapter);
        controller.setStage(stage);
    }

    public ObservableList<FileItem> getItems(){
        return items;
    }

    public void addChangeListener(ChangeEventListener listener){
        listeners.add(listener);
    }

    public void callListners(ChangeEvent event){
        for (ChangeEventListener l :
                listeners) {
            l.onChange(event);
        }
    }
}
