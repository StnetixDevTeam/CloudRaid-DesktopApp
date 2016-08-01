package watchingService;

import java.nio.file.Path;


/**
 * Объект события, который передаётся слушателю
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class WatcherEvent {
    enum EVENT_TYPES{
        ENTRY_CREATE, ENTRY_MODIFY,ENTRY_DELETE;
    }

    private Path file;
    private String eventType;

    public WatcherEvent(Path file, String eventType){
        this.file = file;
        this.eventType = eventType;

    }

    public Path getFile(){
        return file;
    }

    public String  getEventType(){
        return eventType;
    }
}
