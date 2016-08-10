package events;

import model.FileItem;

/**
 * Эти события генерируютя при изменении EFS
 * подписаться на них можно используя метод BrowserApp.addChangeListener(ChangeEventListener l)
 * созданы для отслеживания изменений в EFS и отображения их на диске в папке синхронизации
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class FileBrowserEvent {
    public enum EVENT_TYPES {
        CREATE, MODIFY, DELETE, RENAME
    }

    private EVENT_TYPES type;
    private FileItem file;
    private String oldName;

    public FileBrowserEvent(EVENT_TYPES type, FileItem file) {
        this.type = type;
        this.file = file;
        this.oldName = null;
    }
    public FileBrowserEvent(EVENT_TYPES type, FileItem file, String oldName) {
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
