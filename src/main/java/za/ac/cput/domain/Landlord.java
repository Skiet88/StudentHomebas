package za.ac.cput.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Landlord")
public class Landlord extends User {

    private int numOfPropertiesOwned;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Document> documents;

    protected Landlord() {}

    private Landlord(LandlordBuilder builder) {
        super(builder);
        this.numOfPropertiesOwned = builder.numOfPropertiesOwned;
        this.documents = builder.documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public int getNumOfPropertiesOwned() {
        return numOfPropertiesOwned;
    }

    @Override
    public String toString() {
        return "Landlord{" +
                "numOfPropertiesOwned=" + numOfPropertiesOwned +
                ", documents=" + documents +
                ", userId=" + userId +
                ", name=" + name +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", contact=" + contact +
                '}';
    }

    public static class LandlordBuilder extends User.UserBuilder<LandlordBuilder> {
        private int numOfPropertiesOwned;
        private List<Document> documents;

        public LandlordBuilder setNumOfPropertiesOwned(int numOfPropertiesOwned) {
            this.numOfPropertiesOwned = numOfPropertiesOwned;
            return self();
        }

        public LandlordBuilder setDocuments(List<Document> documents) {
            this.documents = documents;
            return self();
        }

        @Override
        public LandlordBuilder copy(User user) {
            super.copy(user);
            if (user instanceof Landlord) {
                this.numOfPropertiesOwned = ((Landlord) user).numOfPropertiesOwned;
                this.documents = ((Landlord) user).documents;
            }
            return self();
        }

        @Override
        protected LandlordBuilder self() {
            return this;
        }

        @Override
        public Landlord build() {
            return new Landlord(this);
        }
    }
}
