package newWatchingService;

import java.nio.file.Path;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public interface ChangeSyncFolderListener {
    void onChange(EventsConsumer.EVENT_TYPES type, Path source, Path target);
}
