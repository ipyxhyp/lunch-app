package ptr.web.lunch.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Roles")
@NamedQueries({
        @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Roles.findById", query = "SELECT r FROM  Role r WHERE r.id = :id"),
        @NamedQuery(name = "Roles.findByName", query = "SELECT r FROM  Role r WHERE r.name = :name")
})
public class Role implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;


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


    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (!id.equals(role.id)) {
            return false;
        }
        if (!name.equals(role.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
