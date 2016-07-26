package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
 */

public class AppSettings {
    private static final String PROPERTIES_FILE_NAME = "main.ini";
    public static Properties properties = new Properties();

    public static void loadProperties(){
        try {
            properties.load(new FileInputStream(AppSettings.class.getResource("../"+PROPERTIES_FILE_NAME).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveProperties(){

    }


}
