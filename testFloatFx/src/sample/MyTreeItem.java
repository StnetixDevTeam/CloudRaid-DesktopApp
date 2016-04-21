package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import sample.model.FileSystemItem;
import sample.model.Folder;

/**
 * Created by Anton on 20.04.2016.
 */
public class MyTreeItem<String> extends TreeItem<String> {
    public Folder item;
    MyTreeItem(Folder item, String name){
        super(name);
        this.item = item;
    }
    MyTreeItem(Folder item, String name, Node node){
        super(name, node);
        this.item = item;

    }
    public Folder getItem(){
        return item;
    }
}
