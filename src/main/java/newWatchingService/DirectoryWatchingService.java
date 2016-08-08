package newWatchingService;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**Сервис отслеживает изменения в директории синхронизации
 * (для этого надо выбрать в настройках в приложении папку синхронизации)
 *Для отслеживания события переименования сервис реализован паттерном consumer-producer.
 * Стандартные события java.nio watchingService не включают переименования.
 * Переименование выглядит как удаление старого файла и создание нового.
 * Producer отслеживает изменения в папке и добавляет события в очередь
 * Consumer переодически проверяет очередь, обрабатывает события оттуда, определяет было ли переименование
 * и вызывает слушателей. Принцип такой: в очередь добавляются события, но обрабатываются не сразу, а через промежуток
 * DELAY. если в очереди два события и первое ENTRY_DELETE а второе ENTRY_CREATE то значет было переименование. Иначе события
 * интерпретируются как есть. DELAY подобран так что вручную удалить и создать файл так быстро не возможно.
 *
 * TODO добавить обработку копирования, т.к. генерируются 2 события ENTRY_CREATE и ENTRY_MODIFY
 *
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public class DirectoryWatchingService {
    public static volatile boolean stop = false;

    Queue<ChangeEvent> queue;
    EventProducer producer;
    EventsConsumer consumer;


    public DirectoryWatchingService(){
        queue = new ArrayBlockingQueue<>(100);
        try {
            producer = new EventProducer(queue);
            consumer = new EventsConsumer(queue);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        System.out.println("Directory watching service is running!");
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public void stop(){
        stop = true;
        producer.stop();
    }

    public void addConsumerEventListner(ChangeSyncFolderListener listener){
        consumer.addEventListeners(listener);
    }
}
