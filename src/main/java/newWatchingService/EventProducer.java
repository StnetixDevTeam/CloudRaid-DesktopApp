package newWatchingService;

import util.AppSettings;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class EventProducer implements Runnable{
    private Queue<ChangeEvent> queue;
    private Path watchingFile;
    private List<ChangeSyncFolderListener> listeners;
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private boolean trace = false;

    public EventProducer(Queue<ChangeEvent> queue) throws IOException {
        this.queue = queue;
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

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                //System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    //System.out.format("update: %s -> %s\n", prev, dir);
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




    @Override
    public void run() {
        while (!DirectoryWatchingService.stop) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                x.printStackTrace();
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
                System.out.format("NATIVE EVENT: %s: %s\n", event.kind().name(), child);
                queue.add(new ChangeEvent(event.kind(), child));

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        x.printStackTrace();
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
        try {
            watcher.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public void stop(){
        try {
            watcher.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
