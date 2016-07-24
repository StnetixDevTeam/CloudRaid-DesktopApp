package util;

import model.EFSItem;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by Anton on 13.06.2016.
 */
public class HibernateUtil {

    private static final String HIBERNATE_SHOW_SQL = "true";
    private static final String HIBERNATE_HBM2DDL_AUTO = "update";
    private static final String HIBERNATE_DIALECT = SQLiteDialect.class.getCanonicalName();
    private static final String HIBERNATE_CONNECTION_DRIVER = "org.sqlite.JDBC";
    private static final String CONNECTION_URL = "jdbc:sqlite:C:/Users/Anton/IdeaProjects/desctopApp/src/main/resources/db1.s3db";


    private static SessionFactory sessionFactory = buildSessionFactory();

    protected static SessionFactory buildSessionFactory() {
        Configuration configuration = getSqliteConfiguration();
        sessionFactory = createSessionFactory(configuration);
        return sessionFactory;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static Configuration getSqliteConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(EFSItem.class);
        configuration.setProperty("hibernate.dialect", HIBERNATE_DIALECT);
        configuration.setProperty("hibernate.connection.driver_class", HIBERNATE_CONNECTION_DRIVER);
        configuration.setProperty("hibernate.connection.url", CONNECTION_URL);
        configuration.setProperty("hibernate.show_sql", HIBERNATE_SHOW_SQL);
        configuration.setProperty("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
        return configuration;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public static void main(String[] args) {
        System.out.println(SQLiteDialect.class.getCanonicalName());
        buildSessionFactory();
        Session session = getSessionFactory().openSession();
        EFSItem item = session.get(EFSItem.class, (long)15);

        System.out.println(item);
    }
}
