package model;

import java.util.Date;

/**
 * Created by Anton on 19.06.2016.
 */
public interface FileItem {
    public long getId();

    public void setId(long id);

    public long getUid();

    public void setUid(long uid);

    public long getSize();

    public void setSize(long size);

    public String getPath();

    public void setPath(String path);

    public boolean isDir();

    public void setDir(boolean dir);

    public boolean isDeleted();

    public void setDeleted(boolean deleted);

    public String getHash();

    public void setHash(String hash);

    public boolean isThumbExist();

    public void setThumbExist(boolean thumbExist);

    public String getPhotoInfo();

    public void setPhotoInfo(String photoInfo);

    public String getVideoInfo();

    public void setVideoInfo(String videoInfo);

    public Date getModified();

    public void setModified(Date modified);

    public FileItem getParent();

    public void setParent(FileItem parent);

    public String getName();

    public void setName(String name);
}
