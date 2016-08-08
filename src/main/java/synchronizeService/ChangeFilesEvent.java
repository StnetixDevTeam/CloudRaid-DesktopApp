package synchronizeService;


import model.FileItem;
import java.nio.file.Path;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class ChangeFilesEvent {
    public enum EVENT_TYPES {
        CREATE, DELETE, COPY, RENAME, MODIFY
    }

    EVENT_TYPES type;

    private Path syncFolderItem;
    private FileItem EFSItem;
    private String oldName;

    public ChangeFilesEvent(EVENT_TYPES types, Path syncFolderItem, FileItem EFSItem) {
        this.type = types;
        this.syncFolderItem = syncFolderItem;
        this.EFSItem = EFSItem;
        oldName = null;
    }
    public ChangeFilesEvent(EVENT_TYPES types, Path syncFolderItem, FileItem EFSItem, String oldName) {
        this.type = types;
        this.syncFolderItem = syncFolderItem;
        this.EFSItem = EFSItem;
        this.oldName = oldName;
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

    public String getOldName() {
        return oldName;
    }

    @Override
    public String toString() {
        return "ChangeFilesEvent{" +
                "type=" + type +
                ", syncFolderItem=" + syncFolderItem +
                ", EFSItem=" + EFSItem +
                '}';
    }
}
