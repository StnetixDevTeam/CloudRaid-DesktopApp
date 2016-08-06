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

    public ChangeEvent(EVENT_TYPES type, FileItem file) {
        this.type = type;
        this.file = file;
    }

    public EVENT_TYPES getType() {
        return type;
    }

    public FileItem getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "ChangeEvent{" +
                "type=" + type +
                ", file=" + file +
                '}';
    }
}
