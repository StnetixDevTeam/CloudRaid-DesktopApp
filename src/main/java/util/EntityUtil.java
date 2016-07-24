package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 16.06.2016.
 */
public class EntityUtil {
    private static final String CONNECTION_URL = "jdbc:sqlite:"+EntityUtil.class.getResource("../db1.s3db");

    public static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory setUp() throws Exception {
        Map<String, Object> configOverrides = new HashMap<>();
        configOverrides.put("javax.persistence.jdbc.url", CONNECTION_URL);
        // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
        // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
        entityManagerFactory = Persistence.createEntityManagerFactory( "com.cloudraid", configOverrides );

        return entityManagerFactory;

    }

    public static void tearDown() throws Exception {
        entityManagerFactory.close();
    }

}
