package view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.FileItem;

import java.awt.*;

/**
 * Created by Anton on 22.06.2016.
 */
public class FileFlowItem extends VBox {

    FileItem item;
    Label label;
    Tooltip tooltip;

    public FileFlowItem(FileItem item, Image folderImg, Image fileImg) {

        tooltip = new Tooltip();
        ImageView folderView = new ImageView(folderImg);
        ImageView fileView = new ImageView(fileImg);
        this.item = item;
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        label = new Label();
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(60);
        if (item.isDir()) {
            getChildren().add(folderView);
            label.setText(item.getName());

        } else {
            getChildren().add(fileView);
            label.setText(item.getName());
        }
        getChildren().add(label);
        setOnMouseEntered(event -> {
            tooltip.setText(item.getName());
            tooltip.show(this, MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY()+10);
        });
        setOnMouseExited(event -> {
            tooltip.hide();
        });



    }

    public FileItem getItem() {
        return this.item;
    }
    public Label getLabel(){
        return label;
    }

}
