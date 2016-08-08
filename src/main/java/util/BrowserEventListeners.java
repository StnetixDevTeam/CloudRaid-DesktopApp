package util;

import model.FileItem;
import synchronizeService.ChangeFilesEvent;
import synchronizeService.SyncService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Anton on 07.08.2016.
 */
public class BrowserEventListeners {
    static AppSettings settings = AppSettings.getInstance();

    public static Path setCurrentPathInSyncFolder(FileItem i){

        Path syncFolder = Paths.get(settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH));
        String path = i.getPath().replace("root/", "") + FileSystems.getDefault().getSeparator() + i.getName();
        return syncFolder.resolve(path);
    }

    public static void onRenameEFSFIle(FileItem i, String oldName){

        FileItem parent = i.getParent();
        if (parent.isSync()){
            Path newPath = setCurrentPathInSyncFolder(i);
            Path oldPath = Paths.get(settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH))
                    .resolve(i.getPath().replace("root/", "") + FileSystems.getDefault().getSeparator() + oldName);
            if (Files.exists(oldPath)){
                try {
                    oldPath.toFile().renameTo(newPath.toFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else System.out.println("Не могу переименовать файл т.к он не существует. Path: " + oldPath);
        }
    }

    public static void onCreateEFSFile(FileItem i){

        FileItem parent = i.getParent();
        if (parent.isSync()){
            Path currentPath = setCurrentPathInSyncFolder(i);
            if (!Files.exists(currentPath)){
                try {
                    //TODO для файлов нужно реализовать механизм загрузки из облаков
                    if (i.isDir()){
                        Files.createDirectory(currentPath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else System.out.println("Не могу создать файл т.к он существует. Path: " + currentPath);
        }
    }

    public static void onDeleteEFSFile(FileItem i){
        FileItem parent = i.getParent();
        if (parent.isSync()){
            Path currentPath = setCurrentPathInSyncFolder(i);
            if (Files.exists(currentPath)){
                try {
                    Files.delete(currentPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else System.out.println("Не могу удалить файл т.к он не существует. Path: " + currentPath);
        }
    }
}
