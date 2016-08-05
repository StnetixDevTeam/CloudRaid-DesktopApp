package synchronizeService;

import model.DAOFileItem;
import model.FileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Сервис синхронизации EFS и syncFolder на диске
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
     * Метод создаёт структуру папок из EFS В папке синхронизации
     */
    public void createDirectoryStructureFromEFS() throws IOException {
        List<FileItem> syncDirectories = daoFileItem.getSyncDirectories();
        for (FileItem i :
                syncDirectories) {
            String path = i.getPath().replace("root/", "") + "/" + i.getName();
            Path currentPath = syncFolder.resolve(path);
            if (!Files.exists(currentPath)) {
                Files.createDirectories(currentPath);
                System.out.println(currentPath);
            }


        }
    }
}
