package za.ac.cput.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
//he was abstract
public   class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;

    @Embedded
    protected Name name;
    protected String gender;
    protected LocalDate dateOfBirth;
    protected String password;

    @OneToOne
    @JoinColumn(name = "contact_id")
    protected Contact contact;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name=" + name +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", contact=" + contact +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(gender, user.gender) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(password, user.password) && Objects.equals(contact, user.contact) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, gender, dateOfBirth, password, contact, roles);
    }

    protected User() {}

    protected User(UserBuilder<?> builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.gender = builder.gender;
        this.dateOfBirth = builder.dateOfBirth;
        this.password = builder.password;
        this.contact = builder.contact;
        this.roles = builder.roles;
    }

    public Long getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return contact.getEmail();
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Contact getContact() {
        return contact;
    }

    public Set<Role> getRoles() {
        return roles;
    }



    public String getPassword() {
        return password;
    }



    public static abstract class UserBuilder<T extends UserBuilder<T>> {
        protected Long userId;
        protected Name name;
        protected String gender;
        protected LocalDate dateOfBirth;
        protected String password;
        protected Contact contact;
        protected Set<Role> roles;

        public T copy(User user) {
            this.userId = user.userId;
            this.name = user.name;
            this.gender = user.gender;
            this.dateOfBirth = user.dateOfBirth;
            this.password = user.password;
            this.contact = user.contact;
            this.roles = user.roles;
            return self();
        }

        public T setUserId(Long userId) {
            this.userId = userId;
            return self();
        }

        public T setName(Name name) {
            this.name = name;
            return self();
        }

        public T setGender(String gender) {
            this.gender = gender;
            return self();
        }

        public T setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return self();
        }

        public T setPassword(String password) {
            this.password = password;
            return self();
        }

        public T setContact(Contact contact) {
            this.contact = contact;
            return self();
        }

        public T setRoles(Set<Role> roles) {
            this.roles = roles;
            return self();
        }

        protected abstract T self();

        public abstract User build();
    }
}
