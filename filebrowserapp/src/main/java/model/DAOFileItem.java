package model;

import javafx.collections.ObservableList;


/**
 *Interface для класса-прослойки для работы с файловой системой
 *
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */
public interface DAOFileItem {
    /**
    * Метод получает коллекцию, которая будет хранить содержание текущей папки
     * в классе-реализации необходимо создать поле ObservableList<FileItem> items
     * на этой коллекции завязаны все манипуляции с отображением EFS
    * */
    void setContent(ObservableList<FileItem> items);
    /**
     * Метод возвращает содержимое текущей папки
     * @return ObservableList<FileItem>
     * */
    ObservableList<FileItem> getContent(FileItem parent);
    /**
     *Метод поднимается вверх по файловой структуре
     *
     * */
    void goBack();

    /**
     * Метод возвращает текущий открытый каталог
     * @return FileItem
     */
    FileItem getCurrentParent();

    /**
     *метод добавляет элемент в текущий каталог
     * @param name - имя папки
     * @param isDir - флаг является ли папкой
     * @param size - размер
     * @return FileItem - вновь созданный элемент
     */
    FileItem addItem(String name, boolean isDir, long size);

    /**
     * метод обновляет элемент
     * @param item - элемент для изменения
     */
    void update(FileItem item);

    /**
     * удаление элемента
     * @param item - элемент для удаления
     */
    void deleteItem(FileItem item);

    /**
     * возвращает корневой элемент
     * @return корневой элемент
     *
     */
    FileItem getRoot();

    /**
     * метод для закрытия сессии
     */
    void close();
}
