package model;

import javafx.scene.image.Image;
import util.CloudSettingsManager;

/**
 * Created by Anton on 02.07.2016.
 */
public class CloudSettings {
    private long id;
    private String name;
    private long totalSpace;
    private long availableSpace;
    private long usingSpace;
    private long cloudRaidSpace;
    private CloudSettingsManager.Cloud cloud;


    public CloudSettings(){

    }


    public CloudSettings(String name, long totalSpace, long usingSpace, long cloudRaidSpace, CloudSettingsManager.Cloud cloud) {
        this.name = name;
        this.totalSpace = totalSpace;
        this.usingSpace = usingSpace;
        this.cloudRaidSpace = cloudRaidSpace;
        this.availableSpace = cloudRaidSpace-usingSpace;
        this.cloud = cloud;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public long getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(long availableSpace) {
        this.availableSpace = availableSpace;
    }

    public long getUsingSpace() {
        return usingSpace;
    }

    public void setUsingSpace(long usingSpace) {
        this.usingSpace = usingSpace;
    }

    public long getCloudRaidSpace() {
        return cloudRaidSpace;
    }

    public void setCloudRaidSpace(long cloudRaidSpace) {
        this.cloudRaidSpace = cloudRaidSpace;
        availableSpace = cloudRaidSpace-usingSpace;
    }

    public Image getImage() {

        return cloud.getImage();
    }



    public Image getBigImage() {
        return cloud.getBigImage();
    }

    public String getCloudName(){
        return cloud.getName();
    }


    @Override
    public String toString() {
        return name;
    }
}
