package util;

import model.DAOFactory;
import model.DAOFileItem;
import model.EFSItem;
import model.FileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static util.ChangeEFSHandler.settings;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class ChangeSyncFolderHandler {
    public static DAOFileItem dataManager = DAOFactory.getInstance().getDAOFileItem();
    //TODO добавить методы для обработки событий из файловой системы
    public static void onCreateFile(Path file) throws IOException {
        String syncFolder = settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH);
        String diff = file.toString().replace(syncFolder, "root");
        //System.out.println("diff "+diff);
        //System.out.println(Paths.get(diff));
        boolean isDir = Files.isDirectory(file);
        if (isDir){
            dataManager.addItemByPath(diff);
            //FileItem item = new EFSItem(String.valueOf(file.getFileName()), new Date(), isDir, dataManager.getItemByPath())
            //dataManager.addItem(String.valueOf(file.getFileName()), isDir, Files.size(file));
        }


    }
}
