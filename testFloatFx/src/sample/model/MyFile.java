package sample.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Anton on 20.04.2016.
 */
public class MyFile implements FileSystemItem{
    private ObjectProperty<FileType> type;
    private StringProperty name;

    public MyFile(String name){
        this.name = new SimpleStringProperty(name);
    }

    public FileType getType() {
        return type.get();
    }

    public ObjectProperty<FileType> typeProperty() {
        return type;
    }

    public void setType(FileType type) {
        this.type.set(type);
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
}
