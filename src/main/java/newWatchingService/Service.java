package newWatchingService;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Oceanos on 03.08.2016.
 */
public class Service {
    Queue<ChangeEvent> queue;
    EventProducer producer;
    EventsConsumer consumer;

    public Service(){
        queue = new ArrayBlockingQueue<>(100);
        try {
            producer = new EventProducer(queue);
            consumer = new EventsConsumer(queue);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public void addConsumerEventListner(ChangeSyncFolderListener listener){
        consumer.addEventListeners(listener);
    }
}
