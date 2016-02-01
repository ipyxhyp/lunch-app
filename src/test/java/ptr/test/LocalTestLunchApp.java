package ptr.test;

import junit.framework.TestCase;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ptr.web.lunch.dao.impl.LunchDaoImpl;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.ClientDailyOrder;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;
import ptr.web.lunch.services.LunchService;
import ptr.web.lunch.services.impl.LunchServiceImpl;

import java.util.List;

public class LocalTestLunchApp extends TestCase {

    private SessionFactory sessionFactory = null;
    private ServiceRegistry serviceRegistry = null;
    private LunchService lunchService = null;

    @Before
    public void init(){

        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(ClientDailyOrder.class);
        configuration.addAnnotatedClass(DailyMenu.class);
        configuration.addAnnotatedClass(MenuItem.class);
        configuration.addAnnotatedClass(Restaurant.class);
        configuration.configure();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        lunchService = new LunchServiceImpl();
    }

    @Test
    public void testCreateRoles() {

        Session session = null;
        try{
            Role adminRole = new Role("Admin");
            Role userRole = new Role("User");
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(adminRole);
            session.save(userRole);

            session.getTransaction().commit();

            Query findAllQuery = session.getNamedQuery("Roles.findAll");
            List<Role> roleList = findAllQuery.list();
            assertTrue(roleList != null);
            assertTrue(roleList.size() > 0);
            Query findByNameQuery = session.getNamedQuery("Roles.findByName").setParameter("name", "Admin");
            roleList = findByNameQuery.list();
            assertTrue(roleList != null);
            assertTrue(roleList.size() > 0);
            assertTrue(roleList.get(0).getName().equals("Admin"));

            roleList = findByNameQuery.setParameter("name" , "User").list();
            assertTrue(roleList != null);
            assertTrue(roleList.size() > 0);
            assertTrue(roleList.get(0).getName().equals("User"));
        }
        finally{
            session.close();
        }

    }

    @Test
    public void testGetUserRoles() {
        Role admin = new Role("Admin");
        Client client = new Client("adminClient" , admin);
        lunchService.addClient(client);
        lunchService.isAdminRole("adminClient");
    }

}
