package ptr.test;


import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.dto.DailyMenuDTO;
import ptr.web.lunch.dto.LunchItemDTO;
import ptr.web.lunch.exceptions.LunchTimeExpiredException;
import ptr.web.lunch.exceptions.OrderAlreadyConfirmedException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

//@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class LunchServiceTest extends AbstractJUnit4SpringContextTests {


    @Qualifier("lunchService")
    @Autowired
    private LunchService lunchService;

    List<MenuItem> menuItems1 = new ArrayList(5);
    List<MenuItem> menuItems2 = new ArrayList(5);
    DailyMenuDTO dailyMenuDto = null;

    @Before
    public void init(){
        Role adminRole = new Role("Admin");
        Role userRole = new Role("User");

        lunchService.addRole(adminRole);
        lunchService.addRole(userRole);

        Client admin = new Client("adminClient" , adminRole);
        Client user = new Client("userClient" , userRole);
        Client guest = new Client("guest" , userRole);

        lunchService.addClient(admin);
        lunchService.addClient(user);
        lunchService.addClient(guest);

        Restaurant restX = new Restaurant("XRestaurant", "XLocation");
        Restaurant restY = new Restaurant("YRestaurant", "YLocation");

        lunchService.addRestaurant(restX);
        lunchService.addRestaurant(restY);


        MenuItem item1 = new MenuItem("steak", new Double(150));
        menuItems1.add(item1);
        MenuItem item2 = new MenuItem("burger", new Double(120));
        menuItems1.add(item2);
        MenuItem item3 = new MenuItem("fish", new Double(120));
        menuItems1.add(item3);
        MenuItem item4 = new MenuItem("salad", new Double(100));
        menuItems1.add(item4);
        MenuItem item5 = new MenuItem("pasta", new Double(99));
        menuItems1.add(item5);

        MenuItem item6 = new MenuItem("pizza", new Double(150));
        menuItems2.add(item6);
        MenuItem item7 = new MenuItem("schnitzel", new Double(120));
        menuItems2.add(item7);
        MenuItem item8 = new MenuItem("salmon", new Double(120));
        menuItems2.add(item8);
        MenuItem item9 = new MenuItem("paella", new Double(100));
        menuItems2.add(item9);
        MenuItem item10 = new MenuItem("kebab", new Double(99));
        menuItems2.add(item5);

        dailyMenuDto = new DailyMenuDTO("2015-11-17", "XRestaurant");
        dailyMenuDto.setLunchItems(menuItems1);

        dailyMenuDto = new DailyMenuDTO("2015-11-20", "YRestaurant");
        dailyMenuDto.setLunchItems(menuItems2);
    }

    @Test
    public  void checkUserRole(){
        assertTrue(lunchService.isAdminRole("adminClient"));
        assertFalse(lunchService.isAdminRole("userClient"));
    }

    @Test
    public void testAddRole(){

        //TODO not implemented yet
        Role admin = new Role("Admin");
        Role user = new Role("User");

        ArrayList<Long> idList = new ArrayList<Long>(3);
        idList.add(lunchService.addRole(admin));
        idList.add(lunchService.addRole(user));
        for(Long xId : idList){
            assertNotNull(lunchService.findEntityById(Role.class.getName(), xId));
        }
    }

    @Test
    public void testAddClient(){

        Role admin = new Role("Admin");
        Role user = new Role("User");
        lunchService.addRole(admin);
        lunchService.addRole(user);
        Client vasya = new Client("Vasya", admin);
        Client petya = new Client("Petya", user);
        Client fedya = new Client("Fedya", user);
        ArrayList<Long> idList = new ArrayList<Long>(3);
        idList.add(lunchService.addClient(vasya));
        idList.add(lunchService.addClient(petya));
        idList.add(lunchService.addClient(fedya));
        for(Long xId : idList){
            assertNotNull(lunchService.findEntityById(Client.class.getName(), xId));
        }
    }

    @Test
    public void testAddRestaurant(){
        Restaurant fishHouse = new Restaurant("Fish house", "Port 1");
        Restaurant meatHouse = new Restaurant("Meat house", "Road 1");
        assertNotNull(lunchService.findEntityById(Restaurant.class.getName(), lunchService.addRestaurant(fishHouse)));
        assertNotNull(lunchService.findEntityById(Restaurant.class.getName(), lunchService.addRestaurant(meatHouse)));
    }


    @Test
    public void testCreateMenuItems(){

        List<Long> idList = new ArrayList<Long>(menuItems1.size());
        for(MenuItem item : menuItems1){
            idList.add(lunchService.addMenuItem(item));
        }
        for(Long id : idList){
            MenuItem item = lunchService.findMenuItem(id , null);
           assertTrue(id != -1 && item.getId() == id);
        }
    }

    @Test
    public void testCreateDailyMenu() throws ParseException {

        // add daily menu from dailyMenu Dto including menuItems created in init()
       long dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
       assertTrue(dailyMenuId != -1);
       // look for created dailyMenu by Id
       DailyMenu dailyMenu = lunchService.findDailyMenu(dailyMenuId);
       assertTrue(dailyMenuId == dailyMenu.getId());
        // create daily menu on next date
        dailyMenuDto = new DailyMenuDTO("2015-11-20", "XRestaurant");
       // same daily menu can be in different restaurants
       dailyMenuDto.setLunchItems(menuItems1);
       dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
       assertTrue(dailyMenuId != -1);
        // verify if daily menu get saved and exists in DB
       dailyMenu = lunchService.findDailyMenu(dailyMenuId);
       assertTrue(dailyMenu.getId() != -1 && dailyMenuId == dailyMenu.getId());
    }

    @Test
    public void testCreateClientDailyOrder() throws ParseException {

        // add daily menu from dailyMenu Dto including menuItems created in init()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = sdf.parse("2015-11-21");
        dailyMenuDto = new DailyMenuDTO("2015-11-21", "Lunch House");
        dailyMenuDto.setLunchItems(menuItems1);
        String dishName = menuItems1.get(0).getDishName();
        long dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
        assertTrue(dailyMenuId != -1);
        ClientOrderDTO order = new ClientOrderDTO("Lunch House", "Vasya", dishName, orderDate);
        long id = lunchService.addClientDailyOrder(order);
        assertTrue(id > -1);
        // look for created clientDaily order Id
        ClientDailyOrder createdClientOrder = (ClientDailyOrder)lunchService.findEntityById(ClientDailyOrder.class.getName() , id);
        // verify client order values are equal to initial requested values
        assertNotNull(createdClientOrder);
        assertEquals(dishName, createdClientOrder.getMenuItemId().getDishName());
        assertEquals(orderDate, createdClientOrder.getOrderDate());
        assertEquals("Vasya", createdClientOrder.getClientId().getName());
        // look for created dailyMenu by Id
        orderDate = sdf.parse("2015-11-21");
        dishName = menuItems1.get(1).getDishName();
        order = new ClientOrderDTO("Lunch House", "Fedya", dishName , orderDate);
        id = lunchService.addClientDailyOrder(order);
        assertTrue(id > -1);
        // look for created clientDaily order Id
        createdClientOrder = (ClientDailyOrder)lunchService.findEntityById(ClientDailyOrder.class.getName() , id);
        // verify client order values are equal to initial requested values
        assertNotNull(createdClientOrder);
        assertEquals(dishName, createdClientOrder.getMenuItemId().getDishName());
        assertEquals(orderDate, createdClientOrder.getOrderDate());
        assertEquals("Fedya", createdClientOrder.getClientId().getName());
    }

    @Test
    (expected = OrderAlreadyConfirmedException.class )
    public void testUpdateClientDailyOrder(){
        // createDailyOrder
        try{
            // menuItems populated in init() method
            String userName = "John Smith";
            String restaurantName = "Lunch House";
            String orderDateString = "2015-11-22";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDate = sdf.parse(orderDateString);
            dailyMenuDto = new DailyMenuDTO(orderDateString, restaurantName);
            dailyMenuDto.setLunchItems(menuItems1);
            String dishName = menuItems1.get(0).getDishName();
            long dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
            assertTrue(dailyMenuId != -1);
            ClientOrderDTO order = new ClientOrderDTO (restaurantName, userName, dishName, orderDate);
            long id = lunchService.addClientDailyOrder(order);
            assertTrue(id > -1);
            // look for created clientDaily order Id
            ClientDailyOrder createdClientOrder = (ClientDailyOrder)lunchService.findEntityById(ClientDailyOrder.class.getName() , id);
            ClientDailyOrder updatedClientOrder = null;
            // verify client order values are equal to initial requested values
            assertNotNull(createdClientOrder);
            assertEquals(dishName, createdClientOrder.getMenuItemId().getDishName());
            assertEquals(orderDate, createdClientOrder.getOrderDate());
            assertEquals(userName, createdClientOrder.getClientId().getName());
            // update Daily order with different MenuItem for same user, same Restaurant, same Date
            dishName = menuItems1.get(1).getDishName(); // burger dishName from available items in DailyMenu
            ClientOrderDTO updatedClientOrderDto = new ClientOrderDTO (restaurantName, userName, dishName, orderDate);
            long updatedOrderId = lunchService.updateClientDailyOrder(updatedClientOrderDto);
            assertTrue(updatedOrderId > -1);
            updatedClientOrder = (ClientDailyOrder)lunchService.findEntityById(ClientDailyOrder.class.getName(), updatedOrderId);
            assertTrue(updatedClientOrder.getId() == updatedOrderId);
            assertTrue(updatedClientOrder.isConfirmed() == true);

            dishName = menuItems1.get(2).getDishName(); //  dishName from available items in DailyMenu
            updatedClientOrderDto.setMenuItemName(dishName);
            // exception has to be thrown
            lunchService.updateClientDailyOrder(updatedClientOrderDto);

        } catch (ParseException pEx){
                pEx.printStackTrace();
        }
    }


    @Test
    public void checkLunchTime(){
        // skip, TODO impl
    }


    private long addClientDailyOrder(String userName, String restaurantName, String dishName,
            List<MenuItem> menuItems, String orderDateString) throws ParseException{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = sdf.parse(orderDateString);
        DailyMenuDTO dailyMenuDto = new DailyMenuDTO(orderDateString, restaurantName);
        dailyMenuDto.setLunchItems(menuItems);
        long dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
        ClientOrderDTO order = new ClientOrderDTO(restaurantName, userName, dishName, orderDate);
        return lunchService.addClientDailyOrder(order);
    }

}
