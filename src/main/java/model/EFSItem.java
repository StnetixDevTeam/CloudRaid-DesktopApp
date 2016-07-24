package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Anton on 12.06.2016.
 */
@Entity
@Table(name = "filesystem")
public class EFSItem implements FileItem, Comparable<EFSItem>{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UID")
    private long uid;

    @Column(name = "SIZE")
    private long size;

    @Column(name = "PATH")
    private String path;

    @Column(name = "IS_DIR")
    private boolean isDir;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "THUMB_EXIST")
    private boolean thumbExist;

    @Column(name = "PHOTO_INFO")
    private String photoInfo;

    @Column(name = "VIDEO_INFO")
    private String videoInfo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED")
    private Date modified;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "PARENT")
    private EFSItem parent;

    public EFSItem(){

    }

    public EFSItem(EFSItem parent){
        this.parent = parent;
        setPath();
    }

    public EFSItem(String name, Date date, boolean isDir, EFSItem parent){
        this.name = name;
        this.modified = date;
        this.isDir = isDir;

        this.parent = parent;
        setPath();

    }

    public EFSItem(String name, Date date, boolean isDir){
        this.name = name;
        this.modified = date;
        this.isDir = isDir;

        setPath();

    }

    private void setPath(){
        String parentPath;
        if (parent == null) return;
        if (parent.getPath() == null) parentPath = "";
        else parentPath = parent.getPath()+"/";
        this.path = parentPath + parent.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

   public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isThumbExist() {
        return thumbExist;
    }

    public void setThumbExist(boolean thumbExist) {
        this.thumbExist = thumbExist;
    }

    public String getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(String photoInfo) {
        this.photoInfo = photoInfo;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public FileItem getParent() {
        return parent;
    }



    public void setParent(FileItem parent) {
        this.parent = (EFSItem) parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(EFSItem o) {
        if (isDir && !o.isDir()) return -1;
        if (!isDir && o.isDir()) return 1;
        return name.compareToIgnoreCase(o.getName());
    }
}
