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
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
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
