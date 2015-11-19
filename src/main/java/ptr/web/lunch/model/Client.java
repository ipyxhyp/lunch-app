package ptr.web.lunch.model;


import java.io.Serializable;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "Clients")
@NamedQueries({
        @NamedQuery(name = "Clients.findAll", query = "SELECT cl FROM Client cl"),
        @NamedQuery(name = "Clients.findById", query = "SELECT cl FROM  Client cl WHERE cl.id = :id"),
        @NamedQuery(name = "Clients.findByName", query = "SELECT DISTINCT(cl) FROM  Client cl INNER JOIN FETCH cl.roleId WHERE cl.name = :name"),
        @NamedQuery(name = "Clients.findByRole", query = "SELECT cl FROM  Client cl WHERE cl.roleId = :roleId")
})
public class Client implements Serializable {



    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;


    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID",referencedColumnName = "ID")
    private Role roleId;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId", fetch = FetchType.LAZY)
    private Collection<ClientDailyOrder> clientDailyOrderCollection;


    public Client() {
    }

    public Client(String name, Role roleId) {
        this.name = name;
        this.roleId = roleId;
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

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    public Collection<ClientDailyOrder> getClientDailyOrderCollection() {
        return clientDailyOrderCollection;
    }

    public void setClientDailyOrderCollection(Collection<ClientDailyOrder> clientDailyOrderCollection) {
        this.clientDailyOrderCollection = clientDailyOrderCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Client client = (Client) o;

        if (!clientDailyOrderCollection.equals(client.clientDailyOrderCollection)) {
            return false;
        }
        if (!id.equals(client.id)) {
            return false;
        }
        if (!name.equals(client.name)) {
            return false;
        }
        if (!roleId.equals(client.roleId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + roleId.hashCode();
        result = 31 * result + clientDailyOrderCollection.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roleId=" + roleId +
                ", clientDailyOrderCollection=" + clientDailyOrderCollection +
                '}';
    }
}
