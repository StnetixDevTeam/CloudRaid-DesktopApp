package util;

import model.FileItem;
import synchronizeService.ChangeFilesEvent;
import synchronizeService.SyncService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Anton on 07.08.2016.
 */
public class ChangeEFSHandler {
    static AppSettings settings = AppSettings.getInstance();

    public static Path setCurrentPathInSyncFolder(FileItem i){

        Path syncFolder = Paths.get(settings.getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH));
        String path = i.getPath().replace("/root/", "");
        Path newPath = syncFolder.resolve(path);
        System.out.println("new path "+newPath);
        return newPath;
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
                deletePaths(currentPath);
            } else System.out.println("Не могу удалить файл т.к он не существует. Path: " + currentPath);
        }
    }
    private static void deletePaths(Path currentPath){
       try {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentPath)) {
                for (Path path : directoryStream) {
                    if (Files.isDirectory(path)) deletePaths(path);
                    else Files.delete(path);
                }
            } catch (IOException ex) {}

            Files.delete(currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
