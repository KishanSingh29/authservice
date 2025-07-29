package authservice.entities;


import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Getter
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Long phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();

}
