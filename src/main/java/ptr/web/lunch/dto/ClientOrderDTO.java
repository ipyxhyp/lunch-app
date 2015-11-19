package ptr.web.lunch.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientOrderDTO implements Serializable {

    private String restaurantName;
    private String userName;
    private String menuItemName;
    private String orderDate;
    private Date parsedDate;



    public ClientOrderDTO() {
    }

    public ClientOrderDTO(String restaurantName, String userName, String menuItemName, String orderDate) {
        this.restaurantName = restaurantName;
        this.userName = userName;
        this.menuItemName = menuItemName;
        this.orderDate = orderDate;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(Date parsedDate) {
        this.parsedDate = parsedDate;
    }

    @Override
    public String toString() {
        return "ClientOrderDTO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", userName='" + userName + '\'' +
                ", menuItemName='" + menuItemName + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
