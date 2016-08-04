import watchingService.DirectoryWatcher;
import watchingService.WatcherEvent;

import java.io.IOException;
import java.util.List;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class TestDirectoryWatcher {
    public static void main(String[] args) {
        DirectoryWatcher watcher = null;
        try {
            watcher = DirectoryWatcher.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        watcher.addEventListeners(TestDirectoryWatcher::listner);
        try {
            watcher.doWatch();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void listner(WatcherEvent e){
        //TODO думать как организовать отслеживание переименований и правильно обрабатывать удаление
        System.out.println("It works!------");

            System.out.println(e.getEventType());
            System.out.println(e.getFile());

        System.out.println("-------------");
    }
}
