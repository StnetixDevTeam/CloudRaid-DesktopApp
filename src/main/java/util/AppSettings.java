package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**Класс для работы с настройками приложения
 * включает:
 * -папка синхронизации SINCHRONIZATION_PATH
 * -..
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */

public class AppSettings {
    private static final String PROPERTIES_FILE_NAME = "main.ini";
    public static Properties properties = new Properties();
    public static Map<String, String> prop = new HashMap<>();

    public static void loadProperties(){
        try {
            properties.load(new FileInputStream(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.put("SINCHRONIZATION_PATH", properties.getProperty("SINCHRONIZATION_PATH"));
    }
    public static void saveProperties(){
        /**
         * TODO реализовать сохранение настроек. если не вызывать метод, настройки не сохраняются
         */
    }
    public static void setProperty(String key, String val){
        prop.put(key,val);
    }




}
