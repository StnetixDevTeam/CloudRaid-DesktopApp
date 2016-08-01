package watchingService;

import util.AppSettings;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Сервис для отслеживания изменений в папке синхронизации на диске
 *
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class DirectoryWatcher {


    private Path watchingFile;
    private static DirectoryWatcher instance = null;
    private List<ChangeSyncFolderListener> listeners;
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private boolean trace = false;


    private DirectoryWatcher() throws IOException {
        listeners = new ArrayList<>();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        initPath();
        registerAll(watchingFile);
        this.trace = true;

    }

    private void initPath() {
        watchingFile = Paths.get(AppSettings.getInstance().getProperty(AppSettings.PROPERTIES_KEYS.SINCHRONIZATION_PATH));
    }

    public static synchronized DirectoryWatcher getInstance() throws IOException {
        if (instance == null) instance = new DirectoryWatcher();
        return instance;
    }

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public void doWatch()
            throws IOException, InterruptedException {

        for (; ; ) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                for (ChangeSyncFolderListener listener : listeners) {
                    listener.doSome(new WatcherEvent(child, event.kind().name()));

                }

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                    }
                }
            }



            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }



        }
        watcher.close();
    }

    public void close() throws IOException {
        watcher.close();
    }

    public void addEventListeners(ChangeSyncFolderListener listener) {
        listeners.add(listener);
    }


}
