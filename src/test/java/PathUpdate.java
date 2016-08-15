import model.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Anton on 15.08.2016.
 */
public class PathUpdate {
    public static void main(String[] args) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        DAOFileItem dataManager = daoFactory.getDAOFileItem();
        List<FileItem> list = ((DAOFileItemImpl)dataManager).getAllItems();
        list.forEach(fileItem -> {
            String oldPath = fileItem.getPath()!=null?EFSItem.pathSeparator+fileItem.getPath():"";
            String path = oldPath+ EFSItem.pathSeparator+fileItem.getName();
            fileItem.setPath(path);
            dataManager.update(fileItem);
            System.out.println(path);
        });
    }
}
