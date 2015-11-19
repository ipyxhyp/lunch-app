package ptr.web.lunch.dao;

import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.dto.TestDTO;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.ClientDailyOrder;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface LunchDao {

    List<TestDTO> getTestDtoList();


    Object createRestaurant(Restaurant restaurant);

    Object createClient(Client client);

    Object createMenuItem(MenuItem menuITem);

    Object createDailyMenu(DailyMenu dailyMenu);

    long createClientDailyOrder(ClientDailyOrder clientDailyOrder);

    Role findRole(String roleName);

    long updateClientDailyOrder(ClientDailyOrder order);

    Restaurant getRestaurantByName(String restaurantName);

    void saveMenuItems(List<? extends Serializable> menuItems);

    Object createRole(Role role);

//    List<DailyMenu> getRestaurantTodayMenu(String restaurantName);

    List<DailyMenu> getDailyMenuOnDate(String restaurantName, Date date);

//    Object getDailyMenuOnDate(Date date);

    List<String> getUserRoles(String userName);

    MenuItem findMenuItem(long id);


    MenuItem findMenuItem(String name, DailyMenu dailyMenu);

    DailyMenu findDailyMenu(long dailyMenuId);

    Client findClient(String userName);

    ClientDailyOrder findClientDailyOrder(ClientOrderDTO order);
}
