package synchronizeService;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Сервис синхронизации EFS и syncFolder на диске
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class SyncService {
    private Path syncFolder;

    public void syncEFStoFolder(ChangeFilesEvent event){
        if (event.getEFSItem().isDir()){
            Path newPath = syncFolder.resolve(event.getEFSItem().getPath());
            System.out.println(newPath);
            if (!Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)){
                System.out.println("create");
            }
        }
    }
}
