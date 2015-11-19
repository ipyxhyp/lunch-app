package ptr.web.lunch.dto;

import ptr.web.lunch.model.MenuItem;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DailyMenuDTO implements Serializable {

    private String restaurantName;
    private String menuDate;
    private Collection<LunchItemDTO> menuItems;

    public DailyMenuDTO() {
    }

    public DailyMenuDTO(String restaurantName, String menuDate, Collection<LunchItemDTO> menuItems) {
        this.restaurantName = restaurantName;
        this.menuDate = menuDate;
        this.menuItems = menuItems;
    }

    public DailyMenuDTO(String menuDate, String restaurantName) {
        this.menuDate = menuDate;
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(String menuDate) {
        this.menuDate = menuDate;
    }

    public Collection<LunchItemDTO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<LunchItemDTO> menuItems) {
        this.menuItems = menuItems;
    }

    public void setLunchItems(Collection<MenuItem> menuItems) {

        List<LunchItemDTO> lunchItemList = new ArrayList<LunchItemDTO>(10);
        for (MenuItem item : menuItems){
            lunchItemList.add(new LunchItemDTO(item));
        }
        this.menuItems = lunchItemList;
    }

    @Override
    public String toString() {
        return "DailyMenuDTO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", menuDate='" + menuDate + '\'' +
                ", menuItems=" + menuItems +
                '}';
    }
}
