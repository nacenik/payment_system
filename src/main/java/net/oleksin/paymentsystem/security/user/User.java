package net.oleksin.paymentsystem.security.user;

import lombok.*;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.security.user.role.Role;
import net.oleksin.paymentsystem.security.user.role.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude = "roles")
@ToString(exclude = "person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @CreatedDate
    @Column(name = "created")
    private Date created;
    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;
    @Column(name = "email")
    private String email;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private List<Role> roles;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public boolean isAdmin() {
        return roles.stream()
                .allMatch(role -> role.getRoleName().equals("ROLE_ADMIN"));
    }

    public boolean belongToBankAccount(Long personId) {
        return person.getId().equals(personId);
    }

    public boolean isAdminOrBelongToBankAccount(Long id) {
        return isAdmin() || belongToBankAccount(id);
    }

}
