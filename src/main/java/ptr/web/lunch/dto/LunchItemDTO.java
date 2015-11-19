package ptr.web.lunch.dto;


import ptr.web.lunch.model.MenuItem;

import java.io.Serializable;

public class LunchItemDTO implements Serializable {

    private String name;
    private double price;

    public LunchItemDTO(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public LunchItemDTO() {
    }

    public LunchItemDTO(MenuItem menuItem) {
        if(menuItem != null){
            name = menuItem.getDishName();
            price = menuItem.getPrice();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "LunchItemDTO{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
