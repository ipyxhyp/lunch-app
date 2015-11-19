package ptr.web.lunch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Entity
@Table(name = "Restaurants")
@NamedQueries({
        @NamedQuery(name = "Restaurants.findAll", query = "SELECT rest FROM Restaurant rest"),
//        @NamedQuery(name = "Restaurants.findById", query = "SELECT DISTINCT(rest) FROM  Restaurants rest LEFT JOIN FETCH rest.dailyMenus LEFT JOIN FETCH rest.clientDailyOrders WHERE rest.id = :id"),
        @NamedQuery(name = "Restaurants.findById", query = "SELECT rest FROM  Restaurant rest WHERE rest.id = :id"),
        @NamedQuery(name = "Restaurants.findByName", query = "SELECT DISTINCT(rest) FROM  Restaurant rest WHERE rest.name = :name"),
        @NamedQuery(name = "Restaurants.findByLocation", query = "SELECT DISTINCT(rest) FROM  Restaurant rest WHERE rest.location = :location")

})
//SELECT DISTINCT (tc) FROM TrainingCourse tc left join fetch tc.trainingCourseSectionCollection where tc.courseId = :courseId
public class Restaurant implements  Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOCATION")
    private String location;


//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "restaurantId", fetch = FetchType.LAZY)
//    private DailyMenu dailyMenu;



//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurantId", fetch = FetchType.LAZY)
//    private Collection<ClientDailyOrder> clientDailyOrders;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "restaurantId", fetch = FetchType.LAZY)
//    private ClientDailyOrder clientDailyOrders;


    public Restaurant() {
    }

    public Restaurant(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Restaurant(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public DailyMenu getDailyMenu() {
//        return dailyMenu;
//    }
//
//    public void setDailyMenu(DailyMenu dailyMenu) {
//        this.dailyMenu = dailyMenu;
//    }
//
//    public ClientDailyOrder getClientDailyOrders() {
//        return clientDailyOrders;
//    }
//
//    public void setClientDailyOrders(ClientDailyOrder clientDailyOrders) {
//        this.clientDailyOrders = clientDailyOrders;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Restaurant that = (Restaurant) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!location.equals(that.location)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }


}
