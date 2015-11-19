package ptr.web.lunch.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Entity;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ptr.web.lunch.dao.LunchDao;
import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.ClientDailyOrder;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;
import ptr.web.lunch.model.Test;
import ptr.web.lunch.dto.TestDTO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Repository("lunchDaoImpl")
public class LunchDaoImpl extends BaseEntityDaoSupport implements LunchDao {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = Logger.getLogger(LunchDaoImpl.class);

    @Transactional(readOnly = true)
    @Override
    public List<TestDTO> getTestDtoList() {

        List<TestDTO> testDtoList = new ArrayList<TestDTO>(10);
        Session currentSession = getSessionFactory().getCurrentSession();
//        SQLQuery sqlQuery = currentSession.createSQLQuery(reportQuery);
//        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));
        Query query = currentSession.getNamedQuery("Test.findAll");
        List<Test> testList = query.list();
//        Test testObject = (Test) currentSession.get(Test.class, 1);
        if(testList != null && testList.size() > 0){
            testDtoList.add(new TestDTO(testList.get(0)));
        }
        return testDtoList;

    }
    @Transactional
    @Override
    public Object createRestaurant(Restaurant restaurant) {
        return saveEntity("Restaurant", restaurant, (restaurant.getId() == null));
    }

    @Transactional
    @Override
    public Object createClient(Client client) {
        return saveEntity("Client", client, (client.getId() == null));
    }

    @Transactional
    @Override
    public Object createMenuItem(MenuItem menuITem) {
        return saveEntity("MenuItem", menuITem, (menuITem.getId() == null));
    }

    @Transactional
    @Override
    public Object createDailyMenu(DailyMenu dailyMenu) {
        return saveEntity("DailyMenu", dailyMenu, (dailyMenu.getId() == null));
    }

    @Transactional
    @Override
    public Object createRole(Role role) {
        return saveEntity("Role", role, (role.getId() == null));
    }

    @Transactional
    @Override
    public long createClientDailyOrder(ClientDailyOrder clientDailyOrder) {
        //TODO findRestaurant - if not found - return else create new clientDailyOrder
        // TODO find user by userName - if not exists - insertNew User as Client
        // TODO optional - mark menuItem
        ClientDailyOrder createdOrder = (ClientDailyOrder)saveEntity("ClientDailyOrder", clientDailyOrder, (clientDailyOrder == null));
        return createdOrder.getId();
    }


    @Override
    public void saveMenuItems(List<? extends Serializable> menuItemList){
        saveEntityList("MenuItem", menuItemList);
    }

    @Transactional(readOnly = true)
    @Override
    public Role findRole(String roleName) {
        Role userRole = null;
        Session currentSession = null;
        try {
                currentSession = getSessionFactory().getCurrentSession();
                Query query = currentSession.getNamedQuery("Roles.findByName");
                query.setParameter("name", roleName);
                List<Role> roleList = query.list();
                if(roleList != null && roleList.size() > 0){
                   userRole = roleList.get(0);
                }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while getting roles user >>>", ex);
        }
        return userRole;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getUserRoles(String userName) {
        List<String> userRoles = new ArrayList<String>(10);
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            Query query = currentSession.getNamedQuery("Clients.findByName");
            query.setParameter("name", userName);
            List<Client> clientsList = query.list();
            if(clientsList != null){
                for (Client client : clientsList){
                    userRoles.add(client.getRoleId().getName());
                }
            }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while getting roles user >>>", ex);
        }
        return userRoles;
    }

    @Transactional(readOnly = true)
    @Override
    public MenuItem findMenuItem(long id) {
        MenuItem item = null;
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            item = (MenuItem)currentSession.get(MenuItem.class, id);
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while findMenuItem >>>", ex);
        }
        return item;
    }

    @Transactional(readOnly = true)
    @Override
    public MenuItem findMenuItem(String name, DailyMenu dailyMenu) {
        MenuItem item = null;
        Session currentSession = null;
        String queryName = "MenuItems.findByDishName";
        Query query = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            if(dailyMenu == null){
                query = currentSession.getNamedQuery("MenuItems.findByDishName");
                query.setParameter("dishName", name);
            } else {
                query = currentSession.getNamedQuery("MenuItems.findByDailyMenu");
                query.setParameter("dishName", name);
                query.setParameter("dailyMenu", dailyMenu);

            }

            List<MenuItem> menuItems = query.list();
            if(menuItems != null && !menuItems.isEmpty()){
                item =  menuItems.get(0);
            }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while getting roles user >>>", ex);
        }
        return item;
    }


    @Transactional(readOnly = true)
    @Override
    public DailyMenu findDailyMenu(long dailyMenuId) {
        DailyMenu item = null;
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            item = (DailyMenu)currentSession.get(DailyMenu.class, dailyMenuId);
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while findDailyMenu >>>", ex);
        }
        return item;
    }

    @Transactional(readOnly = true)
    @Override
    public Client findClient(String userName) {
        Client client = null;
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            Criteria criteria = currentSession.createCriteria(Client.class).add(Restrictions.eq("name", userName));
            List<Client> clientList = criteria.list();
            if(clientList != null && !clientList.isEmpty() ){
               client = clientList.get(0);
            }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while findDailyMenu >>>", ex);
        }
        return client;
    }

    @Transactional(readOnly = true)
    @Override
    public ClientDailyOrder findClientDailyOrder(ClientOrderDTO order) {
        ClientDailyOrder clientDailyOrder = null;
        Session currentSession = null;
        if(order != null){
            try {
                 currentSession = getSessionFactory().getCurrentSession();
                 Query query = currentSession.getNamedQuery("ClientDailyOrders.findClientDailyOrder");
                 query.setParameter("restaurantName",order.getRestaurantName());
                 query.setParameter("clientName",order.getUserName());
                 query.setParameter("dishName",order.getMenuItemName());
                 query.setParameter("orderDate",order.getOrderDate());
                 List<ClientDailyOrder> clientOrders = query.list();
                 if(clientOrders != null && clientOrders.size() > 0){
                     clientDailyOrder = clientOrders.get(0);
                 }
            } catch (Exception ex){
                logger.error(" <<< Exception thrown while executing findClientDailyOrder >>>", ex);
            }
        }
        return clientDailyOrder;
    }

    @Transactional
    @Override
    public long updateClientDailyOrder(ClientDailyOrder order) {
        long id = -1;

        ClientDailyOrder clientDailyOrder = (ClientDailyOrder)saveEntity("ClientDailyOrder", order, (order.getId() == null));
        if(clientDailyOrder != null){
            id = clientDailyOrder.getId();
        }
        return id;
    }
    /**
    * @Param restaurantName
    *
    * @Return restaurant object found, null if not such restaurant found
    *
    * */
    @Transactional(readOnly = true)
    @Override
    public Restaurant getRestaurantByName(String restaurantName) {
        Restaurant restaurant = null;
        Session currentSession = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            Query query = currentSession.getNamedQuery("Restaurants.findByName");
            query.setParameter("name", restaurantName);
            List<Restaurant> restaurantList = query.list();
            if(restaurantList != null && restaurantList.size() > 0){
                restaurant = restaurantList.get(0);
            }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while getting Restaurants.findByName >>>", ex);
        }
        return restaurant;
    }

    /**
    *  @param restaurantName - if is null then searches only by date
    *  @param date - if is null searches by restaurant on current date
    *                if both are non empty - searches by both parameters
    *  @return  - list containing found DailyMenu objects, list can be empty if not found DailyMenu objects
    * */
    @Transactional(readOnly = true)
    @Override
    public List<DailyMenu> getDailyMenuOnDate(String restaurantName, Date date) {

        List<DailyMenu> dailyMenuList = new ArrayList<DailyMenu>(10);
        Session currentSession = null;
        String queryName = "DailyMenus.findByRestaurantOnDate";
        Query query = null;
        try {
            currentSession = getSessionFactory().getCurrentSession();
            if(StringUtils.isBlank(restaurantName)){
                queryName = "DailyMenus.findByDate";
                query = currentSession.getNamedQuery(queryName).setParameter("menuDate", date);
            } else if (date == null){
                queryName = "DailyMenus.findByRestaurantOnDate";
                query = currentSession.getNamedQuery(queryName).setParameter("restaurantName", restaurantName)
                        .setParameter("menuDate", getShortDate(new Date(System.currentTimeMillis())));
            } else {
                query = currentSession.getNamedQuery(queryName).setParameter("restaurantName", restaurantName)
                        .setParameter("menuDate", date);
            }
             dailyMenuList = query.list();
            if(!dailyMenuList.isEmpty()){
                dailyMenuList.get(0);
            }
        } catch (Exception ex){
            logger.error(" <<< Exception thrown while getting DailyMenus.findByRestaurantOnDate >>>", ex);
        }
        return dailyMenuList;

    }

    private Date getShortDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
