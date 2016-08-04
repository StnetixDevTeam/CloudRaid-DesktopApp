package newWatchingService;

import java.nio.file.Path;

/**
 * Created by Oceanos on 04.08.2016.
 */
public interface ChangeSyncFolderListener {
    void onChange(EventsConsumer.EVENT_TYPES type, Path source, Path target);
}
