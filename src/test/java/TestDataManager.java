import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.EFSItem;
import model.DAOFileItemImpl;

import java.util.List;

/**
 * Created by Anton on 18.06.2016.
 */
public class TestDataManager {
    public static void main(String[] args) throws Exception {
        ObservableList items = FXCollections.observableArrayList();
        DAOFileItemImpl dataManager = new DAOFileItemImpl();

        EFSItem root = dataManager.getRoot();
        System.out.println(root.getName());

        List<EFSItem> res = dataManager.getContent(root);
        System.out.println(res.get(0).getName());

        List<EFSItem> cont = dataManager.getContent(res.get(0));


        System.out.println(cont.get(0).getName());



        //EFSItem newItem = dataManager.addItem("file231.jpg", false);

        //System.out.println(newItem);

        dataManager.goBack();
        //System.out.println(dataManager.currentContent.get(0).getName());

        dataManager.close();
    }
}
