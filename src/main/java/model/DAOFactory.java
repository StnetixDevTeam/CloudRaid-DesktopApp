package model;

import javax.persistence.EntityManager;

/**
 * Created by Anton on 24.07.2016.
 */
public class DAOFactory {
    private static DAOFactory instance = null;
    private static DAOFileItem fileItemDAOImpl = null;
    private static EntityManager entityManager = null;

    public static synchronized DAOFactory getInstance(EntityManager entityManager) {
        if (instance == null) {
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
