package synchronizeService;

import model.DAOFileItem;
import model.FileItem;
import util.ChangeEFSHandler;
import util.ChangeSyncFolderHandler;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Сервис синхронизации EFS и syncFolder на диске
 * Идея такая: все события из EFS и SyncFolder поступают в очередь и обрабатываются этим сервисом
 *
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class SyncService implements Runnable{
    public static Queue<ChangeFilesEvent> queue = new ArrayBlockingQueue<>(100);
    public static volatile boolean stop = false;

    private Path syncFolder;
    private DAOFileItem daoFileItem;

    public SyncService(DAOFileItem daoFileItem, Path syncFolder) {
        this.daoFileItem = daoFileItem;
        this.syncFolder = syncFolder;
    }

    /**
     * метод синхронизует изменения в EFS с папкой синхронизации на диске
     * т.е. при создании папки в EFS в синхронизованой папке эта папка создаётся на диске
     * @param event событие изменений в EFS
     */
    public void syncEFStoFolder(ChangeFilesEvent event) {
        System.out.println("From EFS "+event);
        switch (event.getType()){
            case DELETE:
                ChangeEFSHandler.onDeleteEFSFile(event.getEFSItem());
                break;
            case CREATE:
                ChangeEFSHandler.onCreateEFSFile(event.getEFSItem());
                break;
            case RENAME:
                ChangeEFSHandler.onRenameEFSFIle(event.getEFSItem(), event.getOldName());
                break;
        }
    }

    /**
     * метод синхронизует изменения в папке синхронизации на диске с EFS
     * т.е. при создании папки на диске эта папка создаётся в EFS
     * @param event событие изменений в папке на диске
     */
    public void syncFolderToEFS(ChangeFilesEvent event){
        System.out.println("From FileSystem "+event);
        switch (event.getType()){
            case DELETE:
                break;
            case CREATE:
                try {
                    ChangeSyncFolderHandler.onCreateFile(event.getSyncFolderItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RENAME:
                break;
        }
    }

    /**
     *
     * Метод создаёт структуру из синхронизованных папок  EFS В папке синхронизации на диске
     * FIXME в линуксе не адекватно работает
     */
    public void createDirectoryStructureFromEFS() throws IOException {
        List<FileItem> syncDirectories = daoFileItem.getSyncDirectories();
        for (FileItem i :
                syncDirectories) {
            //FIXME похоже в линуксе не так работает разделитель - /
            String path = i.getPath().replace("root/", "") + FileSystems.getDefault().getSeparator() + i.getName();
            Path currentPath = syncFolder.resolve(path);
            if (!Files.exists(currentPath)) {
                Files.createDirectories(currentPath);
                //System.out.println(currentPath);
            }


        }
    }

    @Override
    public void run() {
        System.out.println("Sync service is running!");
        while (!stop){
            try {
                Thread.sleep(100);
                ChangeFilesEvent e  = queue.poll();
                if (e!=null){
                    //System.out.println("Event from sync manager "+e);
                    if (e.getEFSItem()==null){
                        syncFolderToEFS(e);
                    } else if (e.getSyncFolderItem()==null){
                        syncEFStoFolder(e);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void addEvent(ChangeFilesEvent e){
        queue.add(e);
    }
    public void stop(){
        stop = true;
    }
}
