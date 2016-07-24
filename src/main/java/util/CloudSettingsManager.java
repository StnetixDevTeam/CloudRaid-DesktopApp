package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import model.CloudSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 02.07.2016.
 */
public class CloudSettingsManager {
    ObservableList<CloudSettings> cloudAccounts ;
    Map<String, Cloud> clouds;

    public CloudSettingsManager(){
        cloudAccounts = FXCollections.observableArrayList();
        clouds = new HashMap<>();

        clouds.put("DropBox", new Cloud("DropBox", new Image(getClass().getResourceAsStream("../images/Dropbox16.png")), new Image(getClass().getResourceAsStream("../images/Dropbox32.png"))));
        clouds.put("GoogleDrive", new Cloud("GoogleDrive", new Image(getClass().getResourceAsStream("../images/googledrive16.png")), new Image(getClass().getResourceAsStream("../images/googledrive32.png"))));
        clouds.put("ICloud", new Cloud("ICloud", new Image(getClass().getResourceAsStream("../images/icloud16.png")), new Image(getClass().getResourceAsStream("../images/icloud32.png"))));
        clouds.put("YandexDisc", new Cloud("YandexDisc", new Image(getClass().getResourceAsStream("../images/yandex16.png")), new Image(getClass().getResourceAsStream("../images/yandex32.png"))));
        clouds.put("OneDrive", new Cloud("OneDrive", new Image(getClass().getResourceAsStream("../images/onedrive16.png")), new Image(getClass().getResourceAsStream("../images/onedrive32.png"))));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cloudAccounts.add(new CloudSettings("DropBox", 5000, 3000, 4000, clouds.get("DropBox")));
        cloudAccounts.add(new CloudSettings("GoogleDrive", 3000, 1000, 2000, clouds.get("GoogleDrive")));

    }

    public ObservableList<CloudSettings> getCloudAccounts(){
        return cloudAccounts;
    }

    public void addCloudAccount(CloudSettings cloudSettings){
        cloudAccounts.add(cloudSettings);
    }
    public Map<String, Cloud> getClouds(){
        return clouds;
    }

    public static class Cloud{
        String name;
        Image image;
        Image bigImage;

        public Cloud(String name, Image image, Image bigImage) {
            this.name = name;
            this.image = image;
            this.bigImage = bigImage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Image getBigImage() {
            return bigImage;
        }

        public void setBigImage(Image bigImage) {
            this.bigImage = bigImage;
        }

        @Override
        public String toString() {
            return name;
        }
    }


}
