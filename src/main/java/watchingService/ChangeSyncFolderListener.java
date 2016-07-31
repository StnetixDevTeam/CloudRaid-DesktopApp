package watchingService;

/**
 * Интерфейс слушателя события изменения в папке синхронизации
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public interface ChangeSyncFolderListener {
    void doSome(WatcherEvent event);
}
