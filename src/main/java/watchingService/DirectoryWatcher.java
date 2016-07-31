package watchingService;

import util.AppSettings;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Сервис для отслеживания изменений в папке синхронизации на диске
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class DirectoryWatcher implements Runnable{


    private Path watchingFile;
    private static DirectoryWatcher instance = null;
    private List<ChangeSyncFolderListener> listeners;


    private DirectoryWatcher(){
        listeners = new ArrayList<>();
        initPath();
    }

    private void initPath(){
        watchingFile = Paths.get(AppSettings.getInstance().getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH));
    }

    public static synchronized DirectoryWatcher getInstance(){
        if (instance == null) instance = new DirectoryWatcher();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void doWatch()
            throws IOException, InterruptedException {

        WatchService watchService = FileSystems.getDefault().newWatchService();


        WatchKey watchKey = watchingFile.register(watchService, ENTRY_DELETE, ENTRY_MODIFY, ENTRY_CREATE);

        System.out.println("Watch service registered dir: " + watchingFile.toString());

        for (;;) {

            WatchKey key;

            try {
                System.out.println("Waiting for key to be signalled...");
                key = watchService.take();
            }
            catch (InterruptedException ex) {
                System.out.println("Interrupted Exception");
                return;
            }

            List<WatchEvent<?>> eventList = key.pollEvents();
            System.out.println("Process the pending events for the key: " + eventList.size());

            for (WatchEvent<?> genericEvent: eventList) {

                WatchEvent.Kind<?> eventKind = genericEvent.kind();
                System.out.println("Event kind: " + eventKind);
                if (eventKind == OVERFLOW) {

                    continue; // pending events for loop
                }

                WatchEvent pathEvent = (WatchEvent) genericEvent;
                Path file = (Path) pathEvent.context();
                System.out.println("File name: " + file.toString());
                for (ChangeSyncFolderListener listener: listeners) {
                    listener.doSome(new WatcherEvent(file, eventKind));
                }
            }

            boolean validKey = key.reset();
            System.out.println("Key reset");
            System.out.println("");

            if (! validKey) {
                System.out.println("Invalid key");
                break; // infinite for loop
            }

        } // end infinite for loop

        watchService.close();
        System.out.println("Watch service closed.");
    }

    public void addEventListeners(ChangeSyncFolderListener listener){
        listeners.add(listener);
    }

    @Override
    public void run() {
        try {
            doWatch();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
