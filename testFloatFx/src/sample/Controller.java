package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import sample.model.Account;
import sample.model.FileSystemItem;
import sample.model.Folder;
import sample.model.MyFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<FileSystemItem> items = FXCollections.observableArrayList();
    private ObservableList<FileSystemItem> currentItems = FXCollections.observableArrayList();
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    TreeItem<String> rootNode;
    AnchorPane rootAccountPane;
    boolean isFileBrouserVisible = true;


    TreeView foldersTreeView;

    @FXML
    AnchorPane rightPane;

    @FXML
    VBox accountsListView;

    @FXML
    AnchorPane foldersPane;

    @FXML
    private FlowPane fileBrouser;



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setItems();
        setAccountsListView();
        ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("folderSmall.png")));
        rootNode = new TreeItem<>("Folders", rootIcon);
        rootNode.setExpanded(true);

        for (FileSystemItem item: items) {
            if(item instanceof Folder){
                Folder newItem = (Folder) item;
                MyTreeItem<String> treeItem = new MyTreeItem<>(newItem, newItem.getName());


                rootNode.getChildren().add(treeItem);
            }

        }
        //System.out.println(items);
        //ObservableList it = items;
        foldersTreeView = new TreeView<>(rootNode);
        setItemsEventHandler();

        foldersPane.getChildren().add(foldersTreeView);

        currentItems.addAll(items);

        showFileBrouser();
        setAccountView();

    }

    public void setAccountView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("accoutView.fxml"));


        try {
            rootAccountPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItemsEventHandler(){
        foldersTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!(newValue instanceof MyTreeItem)){
                //System.out.println("it " + it);
                //setItems();
                //currentItems.removeAll(currentItems.get);

                currentItems.clear();
                currentItems.addAll(items);
                showFileBrouser();
            } else {
                //System.out.println(((MyTreeItem<String>) newValue).getItem().getName());
                currentItems.clear();
                currentItems.addAll(((MyTreeItem<String>) newValue).getItem().getItems());
                showFileBrouser();
            }


        });
    }

    public void showFileBrouser(){
        if(!isFileBrouserVisible){
            isFileBrouserVisible = true;
            rightPane.getChildren().clear();
            rightPane.getChildren().add(fileBrouser);
        }

        fileBrouser.getChildren().removeAll(fileBrouser.getChildren());
        for (FileSystemItem item:currentItems) {
            fileBrouser.getChildren().add(setItemView(item));
        }
    }

    public void setItems(){
        Folder imgFolder = new Folder("Images");
        Folder textFolder = new Folder("Docs");
        Folder otherFolder = new Folder("Others");
        Folder catsFolder = new Folder("Cats");
        Folder dogsFolder = new Folder("Dogs");
        MyFile file1 = new MyFile("file1.jpg");
        MyFile file2 = new MyFile("file2.jpg");
        MyFile file3 = new MyFile("file3.jpg");
        MyFile file4 = new MyFile("file4.jpg");
        MyFile file5 = new MyFile("file5.jpg");
        MyFile file6 = new MyFile("file6.jpg");
        MyFile file7 = new MyFile("file7.jpg");
        catsFolder.setFiles(FXCollections.observableArrayList(file1, file2, file3));
        dogsFolder.setFiles(FXCollections.observableArrayList(file1, file2, file3, file4));
        textFolder.setFiles(FXCollections.observableArrayList(file1, file2));
        otherFolder.setFiles(FXCollections.observableArrayList(file1, file2, file3, file6, file6, file7));
        imgFolder.setFolders(FXCollections.observableArrayList(catsFolder, dogsFolder));
        imgFolder.setFiles(FXCollections.observableArrayList(file1, file2, file3, file4,file5));
        items.addAll(imgFolder, textFolder, otherFolder, file1, file2);
    }
    public void setAccounts(){
        Account dropbox = new Account("Dropbox");
        Account google = new Account("GoogleDrive");
        accounts.addAll(dropbox, google);
    }

    public void setAccountsListView(){
        Label dropLabel = new Label("Dropbox", new ImageView(new Image(getClass().getResourceAsStream("drop.png"))));
        dropLabel.setPadding(new Insets(5,5,5,0));

        Label googleLabel = new Label("Google Drive", new ImageView(new Image(getClass().getResourceAsStream("google_drive.png"))));
        googleLabel.setPadding(new Insets(5,5,5,0));
        googleLabel.setOnMouseReleased(event -> {
            showAccontView();
        });
        accountsListView.getChildren().addAll(dropLabel, googleLabel);
    }
    public void showAccontView(){
        isFileBrouserVisible = false;
        rightPane.getChildren().clear();
        rightPane.getChildren().add(rootAccountPane);
    }

    public VBox setItemView(FileSystemItem item){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,10,10,10));
        Image folderImg= new Image(getClass().getResourceAsStream("folder.png"));
        Image fileImg= new Image(getClass().getResourceAsStream("file.png"));
        ImageView folderView = new ImageView(folderImg);
        ImageView fileView = new ImageView(fileImg);
        Label label = new Label();
        if (item instanceof Folder){
            Folder folderItem = (Folder)item;
            vBox.getChildren().add(folderView);
            label.setText(folderItem.getName());
            vBox.setOnMouseReleased(event -> {
                currentItems = folderItem.getItems();
                showFileBrouser();
            });
        } else {
            MyFile fileItem = (MyFile) item;
            vBox.getChildren().add(fileView);
            label.setText(fileItem.getName());
        }
        vBox.getChildren().add(label);



        return vBox;
    }

}
