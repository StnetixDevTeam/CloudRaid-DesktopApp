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

    private final String PROPERTIES_FILE_NAME = "main.ini";
    private Properties properties = new Properties();
    private static AppSettings instance = null;

    private AppSettings(){
        reload();
    }

    public static synchronized AppSettings getInstance(){
        if (instance == null){
            instance = new AppSettings();
        }
        return instance;
    }

    public void reload(){
        try {
            properties.load(new FileInputStream(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getProperty(PROPERTIES_KEYS key){
        return properties.getProperty(key.toString());
    }

    public void saveProperties(){

        try {
            FileWriter writer = new FileWriter(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile());
            properties.store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setProperty(PROPERTIES_KEYS key, String val){

        properties.setProperty(key.toString(), val);
    }


}
