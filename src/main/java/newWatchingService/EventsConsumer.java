package newWatchingService;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by Oceanos on 03.08.2016.
 */
public class EventsConsumer implements Runnable {
    public enum EVENT_TYPES{
        RENAME, MODIFY, DELETE, CREATE
    }

    Queue<ChangeEvent> queue;
    List<ChangeSyncFolderListener> listeners;

    public EventsConsumer(Queue<ChangeEvent> queue){
        listeners = new ArrayList<>();
        this.queue = queue;
    }

    public void addEventListeners(ChangeSyncFolderListener listener){
        listeners.add(listener);
    }


    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(200);
                if (queue.size()>1){
                    System.out.println("RENAME??");
                    ChangeEvent event1 = queue.poll();
                    ChangeEvent event2 = queue.poll();
                    if (event1.getType().equals(ENTRY_DELETE)&&event2.getType().equals(ENTRY_CREATE)){
                        callListeners(EVENT_TYPES.RENAME, event1.getSource(), event2.getSource());
                    } else {
                        callListeners(prepareEvent(event1.getType()), event1.getSource(), null);
                        callListeners(prepareEvent(event2.getType()), event2.getSource(), null);
                    }

                } else {
                    int size = queue.size();
                    for (int i = 0; i < size; i++) {
                        ChangeEvent event = queue.poll();
                        callListeners(prepareEvent(event.getType()), event.getSource(), null);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private EVENT_TYPES prepareEvent(WatchEvent.Kind<?> event){
        if (event.equals(ENTRY_CREATE)) return EVENT_TYPES.CREATE;
        if (event.equals(ENTRY_DELETE)) return EVENT_TYPES.DELETE;
        if (event.equals(ENTRY_MODIFY)) return EVENT_TYPES.MODIFY;
        return null;
    }

    private void callListeners(EVENT_TYPES type, Path source, Path target){
        for (ChangeSyncFolderListener l :
                listeners) {
            l.onChange(type, source, target);
        }
    }
}
