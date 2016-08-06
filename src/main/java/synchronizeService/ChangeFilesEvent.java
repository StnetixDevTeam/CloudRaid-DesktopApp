package synchronizeService;


import model.FileItem;
import java.nio.file.Path;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class ChangeFilesEvent {
    enum EVENT_TYPES {
        CREATE, DELETE, COPY, RENAME, MODIFY
    }

    EVENT_TYPES type;

    private Path syncFolderItem;
    private FileItem EFSItem;

    public ChangeFilesEvent(EVENT_TYPES types, Path syncFolderItem, FileItem EFSItem) {
        this.type = types;
        this.syncFolderItem = syncFolderItem;
        this.EFSItem = EFSItem;
    }

    public EVENT_TYPES getType() {
        return type;
    }

    public Path getSyncFolderItem() {
        return syncFolderItem;
    }

    public FileItem getEFSItem() {
        return EFSItem;
    }
}
