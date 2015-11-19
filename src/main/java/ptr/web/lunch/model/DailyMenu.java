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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "DailyMenus", uniqueConstraints = @UniqueConstraint(columnNames = {"RESTAURANT_ID", "MENU_DATE"}))
@NamedQueries({
        @NamedQuery(name = "DailyMenus.findAll", query = "SELECT dm FROM DailyMenu dm"),
        @NamedQuery(name = "DailyMenus.findById", query = "SELECT dm FROM  DailyMenu dm WHERE dm.id = :id"),
        @NamedQuery(name = "DailyMenus.findByRestaurantId", query = "SELECT dm FROM  DailyMenu dm INNER JOIN fetch dm.restaurantId WHERE dm.restaurantId.id = :restaurantId"),
        @NamedQuery(name = "DailyMenus.findByDate", query = "SELECT  DISTINCT(dm) FROM DailyMenu dm INNER join fetch dm.menuItems INNER JOIN fetch dm.restaurantId WHERE dm.menuDate = :menuDate"),
        @NamedQuery(name = "DailyMenus.findByRestaurantOnDate", query = "SELECT  DISTINCT(dm) FROM DailyMenu dm INNER join fetch dm.menuItems INNER JOIN fetch dm.restaurantId WHERE dm.menuDate = :menuDate AND dm.restaurantId.name = :restaurantName")
//        @NamedQuery(name = "DailyMenus.findByRestaurantOnDate", query = "SELECT  DISTINCT(dm) FROM DailyMenu dm left join fetch dm.trainingCourseSectionCollection WHERE dm.menuDate = :menuDate AND dm.restaurantId.name = :restaurantName")
//        SELECT DISTINCT (tc) FROM TrainingCourse tc left join fetch tc.trainingCourseSectionCollection where tc.courseId = :courseId

})

public class DailyMenu implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID", nullable = false)
    @Column(name = "ID", nullable = false)
//    @Column(name = "DAILY_MENU_ID", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dailyMenu", fetch = FetchType.LAZY)
    private Collection<MenuItem> menuItems;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", referencedColumnName = "ID")
//    private Restaurant restaurantId;
//    @Column(name = "RESTAURANT_ID", nullable = false)
//    @PrimaryKeyJoinColumn
    private Restaurant restaurantId;

    @Column(name = "MENU_DATE", nullable = false)
    private Date menuDate;

    public DailyMenu() {
    }

    public DailyMenu(Long id,  Restaurant restaurantId, Date menuDate) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.menuDate = menuDate;
    }

//    public DailyMenu(Long id,  long restaurantId, Date menuDate) {
//        this.id = id;
//        this.restaurantId = restaurantId;
//        this.menuDate = menuDate;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Collection<MenuItem> menuItems) {
        this.menuItems = menuItems;
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

    public Date getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(Date menuDate) {
        this.menuDate = menuDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DailyMenu dailyMenu = (DailyMenu) o;

        if (!id.equals(dailyMenu.id)) {
            return false;
        }
        if (!menuDate.equals(dailyMenu.menuDate)) {
            return false;
        }
        if (!menuItems.equals(dailyMenu.menuItems)) {
            return false;
        }
        if (!restaurantId.equals(dailyMenu.restaurantId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + menuItems.hashCode();
        result = 31 * result + restaurantId.hashCode();
        result = 31 * result + menuDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DailyMenu{" +
                "id=" + id +
                ", menuItems=" + menuItems.size() +
                ", restaurantId=" + restaurantId +
                ", menuDate=" + menuDate +
                '}';
    }
}
