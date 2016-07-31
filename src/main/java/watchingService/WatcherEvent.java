package watchingService;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Объект события, который передаётся слушателю
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class WatcherEvent {
    private Path file;
    private WatchEvent.Kind<?> eventType;

    public WatcherEvent(Path file, WatchEvent.Kind<?> eventType){
        this.file = file;
        this.eventType = eventType;
    }

    public Path getFile(){
        return file;
    }

    public WatchEvent.Kind<?>  getEventType(){
        return eventType;
    }
}
