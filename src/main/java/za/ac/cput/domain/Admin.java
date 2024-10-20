package za.ac.cput.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {

    protected Admin() {}

    private Admin(AdminBuilder builder) {
        super(builder);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId=" + userId +
                ", name=" + name +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", contact=" + contact +
                '}';
    }

    public static class AdminBuilder extends User.UserBuilder<AdminBuilder> {
        @Override
        public AdminBuilder copy(User user) {
            super.copy(user);
            return self();
        }
        
        @Override
        protected AdminBuilder self() {
            return this;
        }

        @Override
        public Admin build() {
            return new Admin(this);
        }
    }
}
