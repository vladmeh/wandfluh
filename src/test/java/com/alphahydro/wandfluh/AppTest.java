package com.alphahydro.wandfluh;

import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * @autor mvl on 26.12.2017.
 */
public class AppTest extends TestCase {
    private SessionFactory sessionFactory;
    @Override
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @SuppressWarnings({ "unchecked" })
    public void testConnectDataBase(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Check database version
        String versionSql = "select version()";
        String resultVersion = (String) session.createNativeQuery(versionSql).getSingleResult();
        System.out.println(resultVersion);

        String listTablesSql = "SHOW FULL TABLES";
        List listTables = session.createNativeQuery(listTablesSql).list();
        System.out.println(listTables.size());

        session.getTransaction().commit();
        session.close();
    }
}
