package ptr.web.lunch.model;


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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Entity
@Table(name = "ClientDailyOrders", uniqueConstraints = @UniqueConstraint(columnNames = {"CLIENT_ID", "ORDER_DATE"}) )
@NamedQueries({
        @NamedQuery(name = "ClientDailyOrders.findAll", query = "SELECT cdo FROM ClientDailyOrder cdo"),
        @NamedQuery(name = "ClientDailyOrders.findById", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.id = :id"),
        @NamedQuery(name = "ClientDailyOrders.findByMenuItemIdAndClientId", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.menuItemId = :menuItemId and cdo.clientId = :clientId"),
        @NamedQuery(name = "ClientDailyOrders.findByMenuClientIdAndOrderDate", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.clientId = :clientId and cdo.orderDate = :orderDate"),
        @NamedQuery(name = "ClientDailyOrders.findByOrderDate", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.orderDate = :orderDate"),
        @NamedQuery(name = "ClientDailyOrders.findByRestaurantId", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.restaurantId = :restaurantId"),
        @NamedQuery(name = "ClientDailyOrders.findByRestaurantOnToday", query = "SELECT cdo FROM  ClientDailyOrder cdo WHERE cdo.restaurantId = :restaurantId and cdo.orderDate = current_date"),
        @NamedQuery(name = "ClientDailyOrders.findClientDailyOrder", query = "SELECT DISTINCT(cdo) FROM  ClientDailyOrder cdo INNER JOIN cdo.restaurantId INNER JOIN cdo.clientId INNER JOIN cdo.menuItemId  WHERE cdo.clientId.name = :clientName AND cdo.orderDate = :orderDate")

})
public class ClientDailyOrder implements Serializable {

    private static final long serialVersionUID = 1L;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dailyMenu", fetch = FetchType.LAZY)
//    private Collection<MenuItem> menuItems;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", referencedColumnName = "ID")
    private Restaurant restaurantId;

//    @Column(name = "RESTAURANT_ID")
//    private long restaurantId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID")
    private Client clientId;

//    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ITEM_ID", referencedColumnName = "ID")
    private MenuItem menuItemId;

    @Column(name = "ORDER_DATE", nullable = false)
    private Date orderDate;

    @Column(name = "IS_VOTED")
    private boolean isVoted;


    public ClientDailyOrder() {
    }

//    public ClientDailyOrder(Restaurant restaurantId, Client clientId, MenuItem menuItemId, Date orderDate, boolean voted) {
//        this.restaurantId = restaurantId;
//        this.clientId = clientId;
//        this.menuItemId = menuItemId;
//        this.orderDate = orderDate;
//        isVoted = voted;
//    }

    public ClientDailyOrder(Restaurant restaurantId, Client clientId, MenuItem menuItemId, Date orderDate, boolean voted) {
        this.restaurantId = restaurantId;
        this.clientId = clientId;
        this.menuItemId = menuItemId;
        this.orderDate = orderDate;
        isVoted = voted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant getRestaurantId() {
        return restaurantId;
    }



    public void setRestaurantId(Restaurant restaurantId) {
        this.restaurantId = restaurantId;
    }

//    public long getRestaurantId() {
//        return restaurantId;
//    }
//
//    public void setRestaurantId(long restaurantId) {
//        this.restaurantId = restaurantId;
//    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public MenuItem getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(MenuItem menuItemId) {
        // TODO add logic if currentDate is less than 11-00 AM then set menuItemId and setVoted(true)
        // else do nothing
        this.menuItemId = menuItemId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setVoted(boolean voted) {

        isVoted = voted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientDailyOrder that = (ClientDailyOrder) o;

        if (isVoted != that.isVoted) {
            return false;
        }
        if (!clientId.equals(that.clientId)) {
            return false;
        }
        if (!id.equals(that.id)) {
            return false;
        }
        if (!menuItemId.equals(that.menuItemId)) {
            return false;
        }
        if (!orderDate.equals(that.orderDate)) {
            return false;
        }
        if (!restaurantId.equals(that.restaurantId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + restaurantId.hashCode();
        result = 31 * result + clientId.hashCode();
        result = 31 * result + menuItemId.hashCode();
        result = 31 * result + orderDate.hashCode();
        result = 31 * result + (isVoted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientDailyOrder{" +
                "id=" + id +
                ", restaurantId=" + restaurantId.getId() +
                ", clientId=" + clientId.getId() +
                ", menuItemId=" + menuItemId.getDishName() +
                ", orderDate=" + orderDate +
                ", isVoted=" + isVoted +
                '}';
    }
}
