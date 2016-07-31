import watchingService.DirectoryWatcher;
import watchingService.WatcherEvent;

import java.io.IOException;

/**
 * Created by Anton on 31.07.2016.
 */
public class TestDirectoryWatcher {
    public static void main(String[] args) {
        DirectoryWatcher watcher = DirectoryWatcher.getInstance();
        watcher.addEventListeners(TestDirectoryWatcher::listner);
        watcher.run();

    }

    public static void listner(WatcherEvent e){
        System.out.println("It works!------");
        System.out.println(e.getEventType());
        System.out.println(e.getFile());
        System.out.println("-------------");
    }
}
