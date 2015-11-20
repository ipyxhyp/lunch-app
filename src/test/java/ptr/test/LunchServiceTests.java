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
import ptr.web.lunch.dto.DailyMenuDTO;
import ptr.web.lunch.dto.LunchItemDTO;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;
import ptr.web.lunch.services.LunchService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

//@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class LunchServiceTests extends AbstractJUnit4SpringContextTests {


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

        lunchService.addClient(admin);
        lunchService.addClient(user);

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
    public void checkAddRole(){

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
    public void checkAddClient(){

        //TODO not implemented yet
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
    public void checkRestaurant(){
        Restaurant fishHouse = new Restaurant("Fish house", "Port 1");
        Restaurant meatHouse = new Restaurant("Meat house", "Road 1");
        assertNotNull(lunchService.findEntityById(Restaurant.class.getName(), lunchService.addRestaurant(fishHouse)));
        assertNotNull(lunchService.findEntityById(Restaurant.class.getName(), lunchService.addRestaurant(meatHouse)));
    }


    @Test
    public void createMenuItems(){

        List<Long> idList = new ArrayList<Long>(menuItems1.size());
        for(MenuItem item : menuItems1){
            idList.add(lunchService.addMenuItem(item));
        }
        for(Long id : idList){
            MenuItem item = lunchService.findMenuItem(id , null);
           assertTrue(item.getId() == id);
        }
    }

    @Test
    public void createDailyMenu() throws ParseException {

       long dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
       assertTrue(dailyMenuId != -1);
       DailyMenu dailyMenu = lunchService.findDailyMenu(dailyMenuId);
       assertTrue(dailyMenuId == dailyMenu.getId());
       dailyMenuDto = new DailyMenuDTO("2015-11-17", "XRestaurant");
       // same daily menu can be in different restaurants
       dailyMenuDto.setLunchItems(menuItems1);
       dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
       assertTrue(dailyMenuId != -1);
       dailyMenu = lunchService.findDailyMenu(dailyMenuId);
       assertTrue(dailyMenuId == dailyMenu.getId());


    }


    @Test
    public void checkLunchTIme(){

    }

}
