package model;

import util.EntityUtil;

import javax.persistence.EntityManager;

/**
 * Created by Anton on 24.07.2016.
 */
public class DAOFactory {
    private static DAOFactory instance = null;
    private static DAOFileItem fileItemDAOImpl = null;
    private static EntityManager entityManager = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {

            try {
                entityManager = EntityUtil.setUp().createEntityManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = new DAOFactory(entityManager);
        }
        return instance;
    }

    private DAOFactory(EntityManager entityManager){
        DAOFactory.entityManager = entityManager;
    }

    public DAOFileItem getDAOFileItem() {
        if (fileItemDAOImpl == null) {
            fileItemDAOImpl = new DAOFileItemImpl(entityManager);
        }
        return fileItemDAOImpl;
    }
}
