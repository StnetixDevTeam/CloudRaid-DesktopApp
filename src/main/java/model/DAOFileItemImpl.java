package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DAOFileItem;
import model.EFSItem;
import model.FileItem;
import util.EntityUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;

/**
 * Created by Anton on 18.06.2016.
 */
public class DAOFileItemImpl implements DAOFileItem {

    public ObservableList currentContent;

    private EntityManagerFactory entityManagerFactory;

    private FileItem currentParent;

    private EntityManager entityManager;



    public DAOFileItemImpl() throws Exception {
        entityManagerFactory = EntityUtil.setUp();

        entityManager = entityManagerFactory.createEntityManager();
    }
    public DAOFileItemImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EFSItem getRoot(){

        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        List<EFSItem> result = entityManager.createQuery( "from EFSItem where name='root'", EFSItem.class ).getResultList();

        entityManager.getTransaction().commit();

        //entityManager.close();



        return result.get(0);
    }

    @Override
    public void setContent(ObservableList<FileItem> items) {
        currentContent = items;
    }

    public ObservableList getContent(FileItem parent){
        currentParent = parent;
        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        long id = parent.getId();
        String query = "from EFSItem where isDeleted = false and parent=" + id;
        List result = entityManager.createQuery( query, FileItem.class ).getResultList();


        entityManager.getTransaction().commit();

        //entityManager.close();

        currentContent.clear();
        currentContent.addAll(result);
        //System.out.println("Current "+result);
        FXCollections.sort(currentContent);

        return  currentContent;
    }

    public void goBack(){
        //currentContent.clear();
        if (currentParent.getParent()==null) return;
        getContent(currentParent.getParent());


    }

    public EFSItem addItem(EFSItem item){
        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        item.setParent(((EFSItem)currentContent.get(0)).getParent());

        entityManager.persist(item);

        entityManager.getTransaction().commit();

        //entityManager.close();


        return item;

    }

    public FileItem addItem(String name, boolean isDir, long size){
        FileItem item = new EFSItem(name, new Date(), isDir, (EFSItem)currentParent);

        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        item.setParent(currentParent);

        item.setPath(setPath());
        item.setSize(size);

        entityManager.persist(item);

        entityManager.getTransaction().commit();

        //entityManager.close();
        currentContent.add(item);


        return item;
    }

    public void update(FileItem item) {
        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(item);

        System.out.println("update "+item);

        entityManager.getTransaction().commit();

        //entityManager.close();
    }

    @Override
    public void deleteItem(FileItem item) {
        System.out.println("delete");
        item.setDeleted(true);
        update(item);

    }

    private String setPath(){
        //EFSItem parent = (EFSItem) ((EFSItem)currentContent.get(0)).getParent();
        String parentPath;
        if (currentParent.getPath() == null) parentPath = "";
        else parentPath = currentParent.getPath()+"/";
        return parentPath + currentParent.getName();
    }

    public void close() {
        entityManager.close();
        try {
            EntityUtil.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * @return */

    public FileItem getCurrentParent(){
        return currentParent;
    }




}
