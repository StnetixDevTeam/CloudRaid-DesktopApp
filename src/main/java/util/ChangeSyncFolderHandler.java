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

    /**
     * метод для реакции на событие создания файла на диске в SyncFolder
     * @param file файл, созданный на диске
     * @throws IOException
     */
    //TODO добавить методы для обработки событий из файловой системы
    public static void onCreateFile(Path file) throws IOException {
        String syncFolder = settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH);

        String diff = file.toAbsolutePath().toString().replace(syncFolder, EFSItem.pathSeparator+"root");
        diff = diff.replace("\\", "/");

        //если создана папка
        if (Files.isDirectory(file)){
            if (dataManager.getItemByPath(diff)==null){
                EFSItem item = new EFSItem(Paths.get(diff));
                item.setSync(true);
                item.setParent(dataManager.getItemByPath(Paths.get(diff).getParent().toString()));
                dataManager.addItem(item);
            }
            //dataManager.addItemByPath(diff);
            //FileItem item = new EFSItem(String.valueOf(file.getFileName()), new Date(), isDir, dataManager.getItemByPath())
            //dataManager.addItem(String.valueOf(file.getFileName()), isDir, Files.size(file));
        }


    }
}
