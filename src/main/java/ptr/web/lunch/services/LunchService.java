package ptr.web.lunch.services;

import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.dto.DailyMenuDTO;
import ptr.web.lunch.dto.TestDTO;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface LunchService {

    long addRestaurant (Restaurant restaurant);

    long addClient (Client client);

    long addMenuItem (MenuItem item);

    long addDailyMenu (DailyMenuDTO dailyMenu) throws ParseException;

    DailyMenu getDailyMenu(String restaurantName, Date date);

    long addClientDailyOrder (ClientOrderDTO order) throws ParseException;

    long updateClientDailyOrder(ClientOrderDTO order) throws ParseException;

    long addRole(Role role);

    boolean isAdminRole(String userName);

    MenuItem findMenuItem(Long id, String name);

    DailyMenu findDailyMenu(long dailyMenuId);
}
