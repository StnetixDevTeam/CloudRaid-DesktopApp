package util;

import java.io.*;
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
    private static Properties properties = new Properties();

    public static void loadProperties(){

        try {
            properties.load(new FileInputStream(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(PROPERTIES_KEYS key){
        return properties.getProperty(key.toString());
    }

    public static void saveProperties(){

        try {
            FileWriter writer = new FileWriter(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile());
            properties.store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setProperty(PROPERTIES_KEYS key, String val){

        properties.setProperty(key.toString(), val);
    }


}
