package util;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**Класс для работы с настройками приложения
 * включает настройки:
 * -папка синхронизации SINCHRONIZATION_PATH
 * -..
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */

public class AppSettings {
    public enum PROPERTIES_KEYS {
        SINCHRONIZATION_PATH
    }

    private static final String PROPERTIES_FILE_NAME = "main.ini";
    public static Properties properties = new Properties();
    public static Map<PROPERTIES_KEYS, String> prop = new HashMap<>();

    public static void loadProperties(){
        try {
            properties.load(new FileInputStream(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.put(PROPERTIES_KEYS.SINCHRONIZATION_PATH, properties.getProperty(PROPERTIES_KEYS.SINCHRONIZATION_PATH.name()));
    }
    public static void saveProperties(){
        /**
         * TODO реализовать сохранение настроек. если не вызывать метод, настройки не сохраняются
         */
    }
    public static void setProperty(PROPERTIES_KEYS key, String val){
        prop.put(key,val);
    }






}
