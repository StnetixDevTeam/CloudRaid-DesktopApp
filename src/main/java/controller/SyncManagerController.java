package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.layout.AnchorPane;
import model.DAOFileItem;
import model.FileItem;
import org.controlsfx.control.CheckTreeView;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Anton on 05.07.2016.
 */
public class SyncManagerController {
    DAOFileItem dataManager;

    @FXML
    private AnchorPane treeViewPane;

    private CheckTreeView<FileItem> treeView;

    @FXML
    void cancelHandler(ActionEvent event) {

    }

    @FXML
    void saveHandler(ActionEvent event) {

    }

    public void setDataManager(DAOFileItem dataManager){
        this.dataManager = dataManager;
    }

    public void initTreeView(){

        CheckBoxTreeItem<FileItem> root = new CheckBoxTreeItem<FileItem>((FileItem) dataManager.getRoot());
        setTreeChildren(root);
        treeView = new CheckTreeView<>(root);
        treeViewPane.getChildren().add(treeView);
        root.setExpanded(true);
        treeView.setShowRoot(false);

    }

    public void setTreeChildren(CheckBoxTreeItem<FileItem> parent){
        FileItem parentItem = parent.getValue();
        CopyOnWriteArrayList<FileItem> items = new CopyOnWriteArrayList<>(dataManager.getContent(parentItem));
        for (FileItem i :items) {
            if (i.isDir()){
                CheckBoxTreeItem<FileItem> item = new CheckBoxTreeItem<FileItem>(i);
                parent.getChildren().add(item);
                setTreeChildren(item);
            }


        }
    }
    public void init(){
        initTreeView();
    }


}
