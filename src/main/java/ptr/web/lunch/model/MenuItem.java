package ptr.web.lunch.model;


import ptr.web.lunch.dto.LunchItemDTO;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MenuItems")
@NamedQueries({
        @NamedQuery(name = "MenuItems.findAll", query = "SELECT mi FROM MenuItem mi"),
        @NamedQuery(name = "MenuItems.findById", query = "SELECT mi FROM  MenuItem mi WHERE mi.id = :id"),
        @NamedQuery(name = "MenuItems.findByDishName", query = "SELECT mi FROM  MenuItem mi WHERE mi.dishName = :dishName"),
        @NamedQuery(name = "MenuItems.findByDailyMenu", query = "SELECT mi FROM  MenuItem mi WHERE mi.dishName = :dishName AND mi.dailyMenu = :dailyMenu"),
        @NamedQuery(name = "MenuItems.findByPrice", query = "SELECT mi FROM  MenuItem mi WHERE mi.price = :price")

})

public class MenuItem implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DISH_NAME", nullable = false)
    private String dishName;


    @Column(name = "PRICE", nullable = false)
    private Double price;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "DAILY_MENU_ID")
    private DailyMenu dailyMenu;


    public MenuItem() {
    }

    public MenuItem(Long id, String dishName, Double price) {
        this.id = id;
        this.dishName = dishName;
        this.price = price;
    }

    public MenuItem(String dishName, Double price) {
        this.dishName = dishName;
        this.price = price;
    }

    public MenuItem(LunchItemDTO lunchItemDTO) {
        if(lunchItemDTO != null){
            this.dishName = lunchItemDTO.getName();
            this.price = lunchItemDTO.getPrice();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(DailyMenu dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuItem menuItem = (MenuItem) o;

        if (!dailyMenu.equals(menuItem.dailyMenu)) {
            return false;
        }
        if (!dishName.equals(menuItem.dishName)) {
            return false;
        }
        if (!id.equals(menuItem.id)) {
            return false;
        }
        if (!price.equals(menuItem.price)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + dishName.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + dailyMenu.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", price=" + price +
                ", getDailyMenu=" + dailyMenu +
                '}';
    }
}
