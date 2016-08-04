import newWatchingService.EventsConsumer;
import newWatchingService.Service;

import java.nio.file.Path;

/**
 * Created by Oceanos on 04.08.2016.
 */
public class TestNewDirectoryWatcher {
    public static void main(String[] args) {
        Service service = new Service();
        service.addConsumerEventListner(TestNewDirectoryWatcher::listner);
        service.start();

    }

    public static void listner(EventsConsumer.EVENT_TYPES type, Path source, Path target){
        System.out.println("EVENT: "+type);

    }
}
