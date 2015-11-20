package ptr.web.lunch.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptr.web.lunch.dao.LunchDao;
import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.dto.DailyMenuDTO;
import ptr.web.lunch.dto.LunchItemDTO;
import ptr.web.lunch.exceptions.BaseEntitySaveException;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.ClientDailyOrder;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;
import ptr.web.lunch.services.LunchService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("lunchService")
public class LunchServiceImpl implements LunchService {



    private static final Logger logger = Logger.getLogger(LunchServiceImpl.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Qualifier("lunchDaoImpl")
    @Autowired
    private LunchDao lunchDao;



    @Override
    public long addRestaurant(Restaurant restaurant) {
        long id = -1;
        restaurant = (Restaurant)lunchDao.createRestaurant(restaurant);
        if(restaurant != null){
            id = restaurant.getId();
        }
        return id;
    }


    @Override
    public long addClient(Client client) {
        long id = -1;
        client = (Client)lunchDao.createClient(client);
        if(client != null){
            id = client.getId();
        }
        return id;
    }

    @Override
    public long addRole(Role role) {
        long id = -1;
        role = (Role)lunchDao.createRole(role);
        if(role != null){
            id = role.getId();
        }
        return id;
    }

    @Override
    public boolean isAdminRole(String userName) {
        List<String> userRoles = lunchDao.getUserRoles(userName);
        return userRoles.contains("Admin");
    }

    @Override
    public MenuItem findMenuItem(Long id, String name) {
        if(id != null && StringUtils.isBlank(name)){
            return lunchDao.findMenuItem(id);
        } else {
            return lunchDao.findMenuItem(name, null);
        }
    }


    @Override
    public long addMenuItem(MenuItem item) {
        long id = -1;
        item = (MenuItem)lunchDao.createMenuItem(item);
        if(item != null){
            id = item.getId();
        }
        return id;
    }




    @Transactional
    @Override
    public long addDailyMenu(DailyMenuDTO dailyMenuDto) throws ParseException {
        long id = -1;
        long restaurantId = -1;
        if(dailyMenuDto != null){
            Restaurant restaurant = null;
            String restaurantName = dailyMenuDto.getRestaurantName();
            if(StringUtils.isNotBlank(restaurantName)){
                restaurant = lunchDao.getRestaurantByName(restaurantName);
                if(restaurant == null){
                    restaurant = new Restaurant(restaurantName);
                    restaurantId = addRestaurant(restaurant);
                    // addedRestaurant
                }
            }
            if(StringUtils.isNotBlank(dailyMenuDto.getMenuDate())){

                Date menuDate = sdf.parse(dailyMenuDto.getMenuDate());
                logger.debug("=== menuDate : "+menuDate);

                DailyMenu dailyMenuItem = new DailyMenu();
                dailyMenuItem.setRestaurantId(restaurant);
                dailyMenuItem.setMenuDate(menuDate);
                DailyMenu savedDailyMenu = null;
                List<MenuItem> menuItemList = new ArrayList<MenuItem>(10);
                logger.debug("=== dailyMenuDto : "+dailyMenuDto);
                for(LunchItemDTO item : dailyMenuDto.getMenuItems()){
                    logger.debug("=== LunchItemDTO : "+item);
                    MenuItem menuItem = new MenuItem(item.getName(), item.getPrice());
                    menuItem.setDailyMenu(dailyMenuItem);
                    menuItemList.add(menuItem);
                }
                try{
                    dailyMenuItem.setMenuItems(menuItemList);
                    savedDailyMenu = (DailyMenu) lunchDao.createDailyMenu(dailyMenuItem);
                } catch (BaseEntitySaveException besx) {
                    throw new RuntimeException("There is already specified daily menu in restaurant : "+restaurantName+" on date :"+menuDate+", please select other date and restaurant ", besx);
                }

                if(savedDailyMenu != null){
                    id = savedDailyMenu.getId();
                }
            }
        }
        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public DailyMenu getDailyMenu(String restaurantName, Date date) {

        DailyMenu menu = null;
        logger.debug(" === getDailyMenu by restaurantName : "+restaurantName+" and date : "+date);
        List<DailyMenu> menuList = lunchDao.getDailyMenuOnDate(restaurantName, date);
        logger.debug("=== daily menu list : "+ menuList);

        if(menuList != null && !menuList.isEmpty()){
            menu = menuList.get(0);
        }
        return menu;
    }


    @Override
    public DailyMenu findDailyMenu(long dailyMenuId) {
        return lunchDao.findDailyMenu(dailyMenuId);
    }


//    @Transactional
    @Override
    public long addClientDailyOrder(ClientOrderDTO order) throws ParseException {
        long id = -1;
        MenuItem menuItemId = null;
        if(order != null){
            ClientDailyOrder clientDailyOrder = new ClientDailyOrder();
                Date orderDate = order.getParsedDate();
                String clientName = order.getUserName();
                List<DailyMenu> dailyMenuList = lunchDao.getDailyMenuOnDate(order.getRestaurantName(), orderDate);
                if(!dailyMenuList.isEmpty()){
                    DailyMenu dailyMenu = dailyMenuList.get(0);
                    for(MenuItem menuItem : dailyMenu.getMenuItems()){
                        if(StringUtils.equals(menuItem.getDishName(),order.getMenuItemName())){
                           menuItemId  =  menuItem;
                        }
                    }
                    Date dailyMenuDate = dailyMenu.getMenuDate();
                    if (isLunchTimeValid(orderDate, dailyMenuDate)){
                        clientDailyOrder.setOrderDate(orderDate);
                        clientDailyOrder.setVoted(true);
                        clientDailyOrder.setMenuItemId(menuItemId);
                    } else {
                        throw new RuntimeException(" <<< Order time is after 11:00 AM, order will not be accepted >>>");
                    }
                    Role userRole = lunchDao.findRole("User");
                    Client client = lunchDao.findClient(clientName);
                    if(client == null){
                        client = new Client(clientName, userRole);
                    }
                    clientDailyOrder.setClientId((Client)lunchDao.createClient(client));
                    clientDailyOrder.setRestaurantId(dailyMenu.getRestaurantId());
                    try{
                        id = lunchDao.createClientDailyOrder(clientDailyOrder);
                    } catch (BaseEntitySaveException besEx){

                        throw new RuntimeException("Existing client order already exists on requested date "+order, besEx);
                    }
                } else {
                    throw new RuntimeException("Daily menu with items in requested order not found "+order);
                }
        }
        return id;
    }

    @Transactional
    @Override
    public long updateClientDailyOrder(ClientOrderDTO order) throws ParseException{

        long updatedId = -1;
        ClientDailyOrder currentClientOrder = null;
        MenuItem menuItem  = lunchDao.findMenuItem(order.getMenuItemName(), order.getRestaurantName(), order.getParsedDate());
        if (isLunchTimeValid(order.getParsedDate(), menuItem.getDailyMenu().getMenuDate())){
            currentClientOrder = lunchDao.findClientDailyOrder(order);
            if(currentClientOrder != null){
                currentClientOrder.setOrderDate(order.getParsedDate());
                currentClientOrder.setVoted(true);
                currentClientOrder.setMenuItemId(menuItem);
                updatedId = lunchDao.updateClientDailyOrder(currentClientOrder);
            }
        } else {
            throw new RuntimeException(" <<< Order time is after 11:00 AM, order will not be accepted >>>");
        }
        return updatedId;
    }


    public Object findEntityById(String name, Long id){
       return lunchDao.findEntity(name, id);
    }

    public long addClientDailyOrder(ClientDailyOrder order) {

        return lunchDao.createClientDailyOrder(order);

    }


    public long updateClientDailyOrder(ClientDailyOrder order) {
        return lunchDao.updateClientDailyOrder(order);
    }

    /*
    *  check if orderDate before actual lunch time expiration
    *  @param orderDate - requested lunch booking date
    *  @param dailyMenuDate - actual date of available dailyMenu of requested dailyMenu item
    *
    * *
    * */
    private boolean isLunchTimeValid(Date orderDate, Date dailyMenuDate){
        // modify order date to set up actual current HOUR and MINUTE and compare it with requested dailyMenuDate (11:00 AM)
        Calendar cal  = Calendar.getInstance();
        cal.setTime(dailyMenuDate);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 0);
        Date lunchTime = cal.getTime();
        return (orderDate != null && orderDate.before(lunchTime));
    }


    private boolean isTodayLunchTimeValid(){

        Calendar cal  = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 0);
        Date currentDateTime  = new Date(System.currentTimeMillis());
        Date lunchTime = cal.getTime();
        return currentDateTime.before(lunchTime);
    }
}
