package controller;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DAOFileItem;
import model.FileItem;
import org.controlsfx.control.CheckTreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Anton on 05.07.2016.
 */
public class SyncManagerController {
    private DAOFileItem dataManager;
    private List<FileItem> checkedItems;
    private List<FileItem> unCheckedItems;
    private List<CheckBoxTreeItem<FileItem>> selected;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private AnchorPane treeViewPane;

    private CheckTreeView<FileItem> treeView;

    @FXML
    void cancelHandler(ActionEvent event) {
        stage.close();
    }

    @FXML
    void saveHandler(ActionEvent event) {
        applyHandler(event);
        stage.close();
    }

    @FXML
    void applyHandler(ActionEvent e){
        checkedItems.forEach(fileItem -> {
            fileItem.setSync(true);
            dataManager.update(fileItem);
            checkedItems.remove(fileItem);
        });
        unCheckedItems.forEach(fileItem -> {
            fileItem.setSync(false);
            dataManager.update(fileItem);
            unCheckedItems.remove(fileItem);
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
        selected.forEach(item -> item.setSelected(true));

    }

    private void onCheckListener(ListChangeListener.Change<? extends TreeItem<FileItem>> c){

        while (c.next()){

            ///test section
            System.out.println("============================================");
            System.out.println("Change: " + c);
            System.out.println("Added sublist " + c.getAddedSubList());
            System.out.println("Removed sublist " + c.getRemoved());
            System.out.println("List " + c.getList());
            System.out.println("Added " + c.wasAdded() + " Permutated " + c.wasPermutated() + " Removed " + c.wasRemoved() + " Replaced "
                    + c.wasReplaced() + " Updated " + c.wasUpdated());
            System.out.println("============================================");
            ///////////////////////

            for (TreeItem i : c.getAddedSubList()){
                checkedItems.add((FileItem) i.getValue());
            }
            for (TreeItem i : c.getRemoved()){
                unCheckedItems.add((FileItem) i.getValue());
            }


        }

    }

    private void setTreeChildren(CheckBoxTreeItem<FileItem> parent){
        FileItem parentItem = parent.getValue();
        CopyOnWriteArrayList<FileItem> items = new CopyOnWriteArrayList<>(dataManager.getContent(parentItem));
        for (FileItem i :items) {
            if (i.isDir()){
                CheckBoxTreeItem<FileItem> item = new CheckBoxTreeItem<>(i);

                parent.getChildren().add(item);
                setTreeChildren(item);
                if (i.isSync()){
                    /*
                    нельзя сразу сделать setSelected(true) для элемента
                    ибо к ним не привяжется слушатель. Поэтому вначале добавляю их в список
                    и после инициализации TreeView делаю их selected
                     */
                    selected.add(item);
                }
            }


        }
    }
    public void init(){
        checkedItems = new ArrayList<>();
        unCheckedItems = new ArrayList<>();
        selected = new ArrayList<>();
        initTreeView();
    }



}
