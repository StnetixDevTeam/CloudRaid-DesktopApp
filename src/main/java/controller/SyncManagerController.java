package controller;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import model.DAOFileItem;
import model.FileItem;
import org.controlsfx.control.CheckTreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Created by Anton on 05.07.2016.
 */
public class SyncManagerController {
    private DAOFileItem dataManager;
    private List<FileItem> addedItems;

    @FXML
    private AnchorPane treeViewPane;

    private CheckTreeView<FileItem> treeView;

    @FXML
    void cancelHandler(ActionEvent event) {

    }

    @FXML
    void saveHandler(ActionEvent event) {
        addedItems.forEach(fileItem -> {
            fileItem.setSync(true);
            dataManager.update(fileItem);
        });
    }

    public void setDataManager(DAOFileItem dataManager){
        this.dataManager = dataManager;
    }

    private void initTreeView(){

        CheckBoxTreeItem<FileItem> root = new CheckBoxTreeItem<>( dataManager.getRoot());
        setTreeChildren(root);
        treeView = new CheckTreeView<>(root);
        treeViewPane.getChildren().add(treeView);
        root.setExpanded(true);
        treeView.setShowRoot(true);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getCheckModel().getCheckedItems().addListener(this::onCheckListener);

    }

    private void onCheckListener(ListChangeListener.Change<? extends TreeItem<FileItem>> c){
        while (c.next()){
            if (c.wasAdded()){
                for (TreeItem i : c.getAddedSubList()){
                    addedItems.add((FileItem) i.getValue());
                }
            }
        }

    }

    private void setTreeChildren(CheckBoxTreeItem<FileItem> parent){
        FileItem parentItem = parent.getValue();
        CopyOnWriteArrayList<FileItem> items = new CopyOnWriteArrayList<>(dataManager.getContent(parentItem));
        for (FileItem i :items) {
            if (i.isDir()){
                CheckBoxTreeItem<FileItem> item = new CheckBoxTreeItem<>(i);
                if (i.isSync()){
                     item.setSelected(true);
                }
                parent.getChildren().add(item);
                setTreeChildren(item);
            }


        }
    }
    public void init(){
        addedItems = new ArrayList<>();
        initTreeView();
    }


}
