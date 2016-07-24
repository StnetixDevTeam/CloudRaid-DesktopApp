package view;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import model.FileItem;

/**
 * Created by Anton on 20.06.2016.
 */
public class FileTreeItem<String> extends TreeItem<String> {
    FileItem item;

    public FileTreeItem(String name, FileItem item){
        super(name);
        this.item = item;
    }

    public FileItem getItem(){
        return item;
    }
}
