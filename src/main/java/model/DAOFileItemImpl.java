package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.EntityUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * FIXME изменить поле PATH объектов EFSItem на стандартное понятие Path. сейчас Path хранит путь к элементу без его имени, а надо чтобы включало имя
 * @author Cloudraid Dev Team (cloudraid.stnetix.com)
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
    public FileItem getItemByPath(String path) {
        entityManager.getTransaction().begin();
        System.out.println("query "+path);
        path = path.replace("\\", "/");
        //Query query = entityManager.createQuery("SELECT e FROM EFSItem e WHERE e.path = :filePath ");
        List result = entityManager.createQuery( "SELECT e FROM EFSItem e WHERE e.path = :filePath")
                .setParameter("filePath", path).getResultList();

        entityManager.getTransaction().commit();

        //entityManager.close();
        if (result.size()>0){
            return (FileItem) result.get(0);
        } else return null;

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
        FXCollections.sort(currentContent);

        return  currentContent;
    }

    public void goBack(){
        //currentContent.clear();
        if (currentParent.getParent()==null) return;
        getContent(currentParent.getParent());


    }

    @Override
    public List<FileItem> getSyncDirectories() {
        entityManager.getTransaction().begin();
        String query = "from EFSItem where isDeleted = false and sync=true";
        List result = entityManager.createQuery( query, FileItem.class ).getResultList();
        entityManager.getTransaction().commit();
        return result;
    }

    public List<FileItem> getAllItems(){
        entityManager.getTransaction().begin();
        String query = "from EFSItem";
        List result = entityManager.createQuery( query, FileItem.class ).getResultList();
        entityManager.getTransaction().commit();
        return result;
    }

    public FileItem addItem(EFSItem item){
        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        item.setParent(((EFSItem)currentContent.get(0)).getParent());

        entityManager.persist(item);

        entityManager.getTransaction().commit();

        //entityManager.close();


        return item;

    }

    public FileItem addItem(String name, boolean isDir, long size){
        EFSItem item = new EFSItem(name, new Date(), isDir, (EFSItem)currentParent);

        //EntityManager entityManager = entityManagerFactory.createEntityManager();
        //item.setParent(currentParent);

        return addItem(item);
    }
    public FileItem addItemByPath(String path){
        Path filePath = Paths.get(path);
        EFSItem item = new EFSItem(filePath.getFileName().toString(), new Date(), true, (EFSItem) getItemByPath(filePath.getParent().toString()));
        //item.setSize(1234);
        return addItem(item);
    }

    public void update(FileItem item) {
        //EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(item);

        entityManager.getTransaction().commit();

        //entityManager.close();
    }

    @Override
    public void deleteItem(FileItem item) {
        item.setDeleted(true);
        update(item);
        String query = "from EFSItem where isDeleted = false and parent=" + item.getId();
        List<FileItem> result = entityManager.createQuery( query, FileItem.class ).getResultList();
        if (result.size()>0){
            for (FileItem i :
                    result) {
                deleteItem(i);
            }
        }


    }

    @Override
    public List<FileItem> getDeletedItems() {
        entityManager.getTransaction().begin();
        String query = "from EFSItem where isDeleted = true";
        List result = entityManager.createQuery( query, FileItem.class ).getResultList();
        entityManager.getTransaction().commit();
        return result;
    }

    private String setPath(){

        //EFSItem parent = (EFSItem) ((EFSItem)currentContent.get(0)).getParent();
        String parentPath;
        if (currentParent.getPath() == null) parentPath = "";
        else parentPath = currentParent.getPath()+"/";
        System.out.println("set Path" + parentPath + currentParent.getName());
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

    public FileItem addItem(FileItem item){
        entityManager.getTransaction().begin();

        if (item.getPath() == null) item.setPath(setPath());
        //item.setSize(size);

        entityManager.persist(item);

        entityManager.getTransaction().commit();

        //entityManager.close();
        System.out.println(item);
        currentContent.add(item);
        if (item.getParent().isSync()) item.setSync(true);


        return item;
    }




}
