package events;

import model.FileItem;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class ChangeEvent {
    public enum EVENT_TYPES {
        CREATE, MODIFY, DELETE, RENAME
    }

    private EVENT_TYPES type;
    private FileItem file;
    private String oldName;

    public ChangeEvent(EVENT_TYPES type, FileItem file) {
        this.type = type;
        this.file = file;
        this.oldName = null;
    }
    public ChangeEvent(EVENT_TYPES type, FileItem file, String oldName) {
        this.type = type;
        this.file = file;
        this.oldName = oldName;
    }


    public EVENT_TYPES getType() {
        return type;
    }

    public FileItem getFile() {
        return file;
    }

    public String getOldName() {
        return oldName;
    }

    @Override
    public String toString() {
        return "ChangeEvent{" +
                "type=" + type +
                ", file=" + file +
                '}';
    }
}
