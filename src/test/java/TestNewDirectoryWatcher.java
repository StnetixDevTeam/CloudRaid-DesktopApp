import newWatchingService.EventsConsumer;
import newWatchingService.DirectoryWatchingService;

import java.nio.file.Path;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class TestNewDirectoryWatcher {
    public static void main(String[] args) {
        DirectoryWatchingService directoryWatchingService = new DirectoryWatchingService();
        directoryWatchingService.addConsumerEventListner(TestNewDirectoryWatcher::listner);
        directoryWatchingService.start();

    }

    public static void listner(EventsConsumer.EVENT_TYPES type, Path source, Path target){
        System.out.println("EVENT: "+type);

    }
}
