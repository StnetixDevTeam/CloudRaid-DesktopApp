package synchronizeService;

import model.DAOFileItem;
import model.FileItem;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Сервис синхронизации EFS и syncFolder на диске
 * Идея такая: все события из EFS и SyncFolder поступают в очередь и обрабатываются этим сервисом
 *
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class SyncService {
    private Path syncFolder;
    private DAOFileItem daoFileItem;

    public SyncService(DAOFileItem daoFileItem, Path syncFolder) {
        this.daoFileItem = daoFileItem;
        this.syncFolder = syncFolder;
    }

    /**
     * метод синхронизует изменения в EFS с папкой синхронизации на диске
     * т.е. при создании папки в EFS в синхронизованой папке эта папка создаётся на диске
     * FIXME пока функциональность дублируется с util.BrowserEventListeners
     * @param event событие изменений в EFS
     */
    public void syncEFStoFolder(ChangeFilesEvent event) {
        if (event.getEFSItem().isDir()) {
            Path newPath = syncFolder.resolve(event.getEFSItem().getPath());
            System.out.println(newPath);
            if (!Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("create");
            }
        }
    }

    /**
     * метод синхронизует изменения в папке синхронизации на диске с EFS
     * т.е. при создании папки на диске эта папка создаётся в EFS
     * @param event событие изменений в папке на диске
     */
    public void syncFolderToEFS(ChangeFilesEvent event){
        if (Files.isDirectory(event.getSyncFolderItem())){

        }
    }

    /**
     *
     * Метод создаёт структуру из синхронизованных папок  EFS В папке синхронизации на диске
     * FIXME в линуксе не адекватно работает
     */
    public void createDirectoryStructureFromEFS() throws IOException {
        List<FileItem> syncDirectories = daoFileItem.getSyncDirectories();
        for (FileItem i :
                syncDirectories) {
            //FIXME похоже в линуксе не так работает разделитель - /
            String path = i.getPath().replace("root/", "") + FileSystems.getDefault().getSeparator() + i.getName();
            Path currentPath = syncFolder.resolve(path);
            if (!Files.exists(currentPath)) {
                Files.createDirectories(currentPath);
                System.out.println(currentPath);
            }


        }
    }
}
