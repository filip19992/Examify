package pl.examify.Examify.user.roles;

import jakarta.persistence.*;
import pl.examify.Examify.user.roles.Role;

import java.util.Collection;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }

    public Privilege() {

    }

    public String getName() {
        return name;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
}
