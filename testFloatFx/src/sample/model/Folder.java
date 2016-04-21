package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Created by Anton on 20.04.2016.
 */
public class Folder  implements FileSystemItem{
    private ObservableList<FileSystemItem> files;
    private ObservableList<FileSystemItem> folders;
    private StringProperty name;
    private ObservableList<FileSystemItem> items;

    public Folder(String name){
        this.name = new SimpleStringProperty(name);
        folders = FXCollections.observableArrayList();
        files = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();
    }

    public ObservableList<FileSystemItem> getFiles() {
        return files;
    }

    public void setFiles(ObservableList<FileSystemItem> files) {
        this.files = files;
        this.items.addAll(files);
    }

    public ObservableList<FileSystemItem> getFolders() {
        return folders;
    }

    public void setFolders(ObservableList<FileSystemItem> folders) {
        this.folders = folders;
        setItems(folders);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public void setItems(ObservableList<FileSystemItem> items){
        this.items.addAll(items);
    }
    public ObservableList<FileSystemItem> getItems(){
        return items;
    }
}
