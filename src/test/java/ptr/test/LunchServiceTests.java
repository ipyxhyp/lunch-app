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
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

//@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class LunchServiceTests extends AbstractJUnit4SpringContextTests {


    @Qualifier("lunchService")
    @Autowired
    private LunchService lunchService;

    List<MenuItem> menuItems = new ArrayList(5);
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
        menuItems.add(item1);
        MenuItem item2 = new MenuItem("burger", new Double(120));
        menuItems.add(item2);
        MenuItem item3 = new MenuItem("fish", new Double(120));
        menuItems.add(item3);
        MenuItem item4 = new MenuItem("salad", new Double(100));
        menuItems.add(item4);
        MenuItem item5 = new MenuItem("pasta", new Double(99));
        menuItems.add(item5);

        dailyMenuDto = new DailyMenuDTO("2015-11-17", "XRestaurant");
        dailyMenuDto.setLunchItems(menuItems);

    }

    @Test
    public  void checkUserRole(){
        assertTrue(lunchService.isAdminRole("adminClient"));
        assertFalse(lunchService.isAdminRole("userClient"));
    }

    @Test
    public void createMenuItems(){

        List<Long> idList = new ArrayList<Long>(menuItems.size());
        for(MenuItem item : menuItems){
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
       dailyMenuDto = new DailyMenuDTO("2015-11-17", "YRestaurant");
       // same daily menu can be in different restaurants
       dailyMenuDto.setLunchItems(menuItems);
       dailyMenuId = lunchService.addDailyMenu(dailyMenuDto);
       assertTrue(dailyMenuId != -1);
       dailyMenu = lunchService.findDailyMenu(dailyMenuId);
       assertTrue(dailyMenuId == dailyMenu.getId());


    }


    @Test
    public void checkLunchTIme(){

    }

}
