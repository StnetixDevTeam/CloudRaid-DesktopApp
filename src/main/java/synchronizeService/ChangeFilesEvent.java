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

    EVENT_TYPES types;

    private Path syncFolderItem;
    private FileItem EFSItem;

    public ChangeFilesEvent(EVENT_TYPES types, Path syncFolderItem, FileItem EFSItem) {
        this.types = types;
        this.syncFolderItem = syncFolderItem;
        this.EFSItem = EFSItem;
    }

    public EVENT_TYPES getTypes() {
        return types;
    }

    public Path getSyncFolderItem() {
        return syncFolderItem;
    }

    public FileItem getEFSItem() {
        return EFSItem;
    }
}
