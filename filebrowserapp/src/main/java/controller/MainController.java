package controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DAOFileItem;
import model.FileItem;
import view.FileFlowItem;
import view.FileTreeItem;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Created by Anton on 16.06.2016.
 */
public class MainController implements Initializable {

    private ObservableList<FileItem> items;
    private FileItem root;
    private DAOFileItem dataAdapter;
    private TreeView treeView;
    private FileItem current;
    private FileFlowItem currentFlowItem;
    private Stage stage;


    private Image folderImg = new Image(getClass().getResourceAsStream("../images/folder.png"));
    private Image fileImg = new Image(getClass().getResourceAsStream("../images/file.png"));


    @FXML
    VBox browserContainer;

    @FXML
    AnchorPane treeViewColumn;

    @FXML
    FlowPane flowViewColumn;

    @FXML
    private TableView<FileItem> tableView;

    @FXML
    private TableColumn<FileItem, String> nameColumn;

    @FXML
    private TableColumn<FileItem, String> sizeColumn;

    @FXML
    private TableColumn<FileItem, Date> changedColumn;

    @FXML
    private TableColumn<FileItem, String> typeColumn;

    @FXML
    private AnchorPane flowAnchorPane;

    @FXML
    private AnchorPane tableAnchorPane;

    @FXML
    private SplitPane flowTablePane;

    private ContextMenu contextMenu;

    @FXML
    void goBackHandler(ActionEvent event) {
        dataAdapter.goBack();
    }

    @FXML
    void addFileHandler(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            dataAdapter.addItem(selectedFile.getName(), false, selectedFile.length());
        }
    }

    @FXML
    private void deleteItemHandler(ActionEvent event){
        if (current!=null){
            dataAdapter.deleteItem(current);
            items.remove(current);
            current = null;
        }
    }

    @FXML
    void createFolderHandler(){
        FileItem item = dataAdapter.addItem("New folder", true, 0);
    }

    @FXML
    void toggleFlowViewHandler(){
        if (flowAnchorPane.isDisable()) {
            flowAnchorPane.setDisable(false);
            flowAnchorPane.setVisible(true);
            flowTablePane.setDividerPositions(1);

        } else {
            flowAnchorPane.setDisable(true);
            flowAnchorPane.setVisible(false);
            flowTablePane.setDividerPositions(0);
        }
    }

    private void renameHandler (ActionEvent event){
        renameItem();
    }

    private void tableViewHandler(MouseEvent event){
        if (event.getButton().equals(MouseButton.PRIMARY)){
            if (event.getClickCount() == 2){
                FileItem selected = tableView.getSelectionModel().getSelectedItem();
                current = selected;
                dataAdapter.getContent(selected);
            }
            if (event.getClickCount() == 1){
                current = tableView.getSelectionModel().getSelectedItem();

            }
        }
        if (event.getButton().equals(MouseButton.SECONDARY)){
            current = tableView.getSelectionModel().getSelectedItem();
            contextMenu.show(tableView, MouseInfo.getPointerInfo().getLocation().getX()+10, MouseInfo.getPointerInfo().getLocation().getY());
            //Node node = tableView.getSelectionModel()
            //contextMenu.show(parent, Side.RIGHT, 10, 10);
        }
    }

    private void flowViewHandler(MouseEvent event){
        Node target = (Node) event.getTarget();
        FileFlowItem parent;
        try{
            parent = (FileFlowItem) target.getParent();
        } catch (Exception e){
            return;
        }


        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 1){

                for (Node item :flowViewColumn.getChildren()) {
                    ((FileFlowItem)item).setBackground(null);
                }
                currentFlowItem = parent;

                current = parent.getItem();
                parent.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,255,0.1), null, null)));

            }
            if (event.getClickCount() == 2) {

                currentFlowItem = parent;
                //System.out.println(parent.getItem());
                dataAdapter.getContent(parent.getItem());
            }
        }
        if (event.getButton().equals(MouseButton.SECONDARY)){

            currentFlowItem = parent;
            current = parent.getItem();
            parent.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,255,0.1), null, null)));
            contextMenu.show(parent, Side.RIGHT, 10, 10);
        }
    }

    private void treeViewHandler(MouseEvent event){
        System.out.println(((FileTreeItem) treeView.getSelectionModel().getSelectedItem()).getItem().getName());
        FileTreeItem selected = (FileTreeItem) treeView.getSelectionModel().getSelectedItem();
        ObservableList list = dataAdapter.getContent(selected.getItem());
        selected.getChildren().clear();
        for (Object i : list) {
            if (!((FileItem) i).isDir()) continue;
            selected.getChildren().add(new FileTreeItem<String>(((FileItem) i).getName(), (FileItem) i));
        }
    }

    private void renameItem(){
        TextInputDialog dialog = new TextInputDialog("New Folder");
        dialog.setTitle("Rename Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter new name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            current.setName(result.get());
        }


        dataAdapter.update(current);

        updateFlowView();
        tableView.refresh();
    }




    private void initTreeView() {

        //FileTreeItem rootItem = new FileTreeItem("root", root);
        TreeItem<String> rootItem = new TreeItem<>("root");
        for (FileItem item : items) {
            if (!item.isDir()) {
                continue;
            }
            FileTreeItem<String> i = new FileTreeItem<>(item.getName(), item);
            rootItem.getChildren().add(i);
        }
        rootItem.setExpanded(true);
        treeView = new TreeView(rootItem);
        treeViewColumn.getChildren().add(treeView);
        treeView.setShowRoot(false);

        treeView.setOnMouseReleased(this::treeViewHandler);

    }


    public void init(ObservableList<FileItem> items, final DAOFileItem dataAdapter) {
        this.items = items;
        this.dataAdapter = dataAdapter;

        flowViewColumn.setOnMouseClicked(this::flowViewHandler);

        items.addListener(new ListChangeListener<FileItem>() {
            public void onChanged(Change<? extends FileItem> c) {
                if (root == null) {
                    root = c.getList().get(0).getParent();
                    initTreeView();
                    initTableView();
                }
                updateFlowView();
                tableView.refresh();

            }
        });

        tableView.setOnMouseReleased(this::tableViewHandler);

    }

    private void initTableView(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        changedColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));

        tableView.setItems(items);

        changedColumn.setCellFactory(new Callback<TableColumn<FileItem, Date>, TableCell<FileItem, Date>>() {
            @Override
            public TableCell<FileItem, Date> call(TableColumn<FileItem, Date> param) {
                return new TableCell<FileItem, Date>(){
                    @Override
                    protected void updateItem(Date item, boolean empty){
                        super.updateItem(item, empty);
                        if (item==null || empty){
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (getTableRow().getItem()!=null){
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String date = sdf.format(item);
                                setText(date);
                            }
                        }
                    }
                };
            }
        });


        typeColumn.setCellFactory(new Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>>() {
            public TableCell<FileItem, String> call(TableColumn<FileItem, String> param) {
                return new TableCell<FileItem, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if (item==null || empty){
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (getTableRow().getItem()!=null){
                                if(((FileItem)getTableRow().getItem()).isDir()){
                                    setText("Folder");
                                } else {
                                    String fileType = (((FileItem)getTableRow().getItem()).getName()).split("\\.")[1];
                                    setText(fileType+" file");
                                }
                            }
                        }
                    }
                };
            }
        });

        nameColumn.setCellFactory(new Callback<TableColumn<FileItem, String>, TableCell<FileItem, String>>() {
            public TableCell<FileItem, String> call(TableColumn<FileItem, String> param) {
                return new TableCell<FileItem, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if (item==null || empty){
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (getTableRow().getItem()!=null){
                                if(((FileItem)getTableRow().getItem()).isDir()){
                                    setGraphic(new ImageView(folderImg));
                                } else {
                                    setGraphic(new ImageView(fileImg));
                                }
                                setText(item);
                            }
                        }
                    }
                };
            }
        });
    }

    private void updateFlowView() {

        flowViewColumn.getChildren().clear();
        for (FileItem item : items) {
            FileFlowItem i = new FileFlowItem(item, folderImg, fileImg);
            flowViewColumn.getChildren().add(i);
        }

    }

    private void initContextMenu(){
        contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::deleteItemHandler);
        contextMenu.getItems().add(deleteItem);

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(this::renameHandler);
        contextMenu.getItems().add(renameItem);
    }





    public VBox getBrowserContainer(){
        return browserContainer;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }


    public void initialize(URL location, ResourceBundle resources) {
        initContextMenu();
    }
}
