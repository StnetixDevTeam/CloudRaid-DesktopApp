import javafx.collections.FXCollections;
import model.DAOFileItemImpl;
import model.FileItem;

import java.util.List;

/**
 * Удалить из базы детей удалённых папок
 */
public class TestNormalizeDeletedItem {
    public static void main(String[] args) {
        try {
            DAOFileItemImpl daoFileItem = new DAOFileItemImpl();
            daoFileItem.setContent(FXCollections.observableArrayList());
            List<FileItem> deleted = daoFileItem.getDeletedItems();
            for (FileItem i :
                    deleted) {
                List<FileItem> child = daoFileItem.getContent(i);
                for (FileItem j:child) {
                    daoFileItem.deleteItem(j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
