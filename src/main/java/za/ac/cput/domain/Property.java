package za.ac.cput.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyID;
    private  String propertyName;
    private  int numberOfRooms;
    private double price;

    @Embedded
    private Status status;

    @OneToOne
    private Address address;

    @ManyToOne
    private Landlord landlord;

    @OneToMany( fetch = FetchType.EAGER)
    private List<Document> pictures;

    protected Property() {
    }


    private Property(Builder builder){
        this.propertyID = builder.propertyID;
        this.propertyName = builder.propertyName;
        this.numberOfRooms = builder.numberOfRooms;
        this.address = builder.address;
        this.landlord = builder.landlord;
        this.pictures = builder.pictures;
        this.price= builder.price;
        this.status= builder.status;
    }

    public Long getPropertyID() {
        return propertyID;
    }

    public String getPropertyName() {
        return propertyName;
    }



    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public Address getAddress() {
        return address;
    }
    public Landlord getLandlord() {
        return landlord;
    }
    public List<Document> getPictures() {
        return pictures;
    }
    public double getPrice() {return price;}
    public Status getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Property{" +
                "propertyID='" + propertyID + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", address=" + address +
                ", landlord=" + landlord +
                ", pictures=" + pictures +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return numberOfRooms == property.numberOfRooms && Double.compare(price, property.price) == 0 && Objects.equals(propertyID, property.propertyID) && Objects.equals(propertyName, property.propertyName) && Objects.equals(status, property.status) && Objects.equals(address, property.address) && Objects.equals(landlord, property.landlord) && Objects.equals(pictures, property.pictures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyID, propertyName, numberOfRooms, price, status, address, landlord, pictures);
    }

    public static class Builder{

        private long propertyID;
        private  String propertyName;
        private  int numberOfRooms;
        private Address address;
        private Landlord landlord;
        private List<Document> pictures;
        private double price;
        private Status status;

        public Builder(){
        }

        public Builder setLandlord(Landlord landlord) {
            this.landlord = landlord;
            return this;
        }

        public Builder setPictures(List<Document> pictures) {
            this.pictures = pictures;
            return this;
        }

        public Builder setPropertyID(long propertyID){
            this.propertyID = propertyID;
            return this;
        }
        public Builder setPropertyName(String propertyName){
            this.propertyName = propertyName;
            return this;
        }

        public Builder setNumberOfRooms(int numberOfRooms){
            this.numberOfRooms = numberOfRooms;
            return this;
        }

        public Builder setAddress(Address address){
            this.address = address;
            return this;
        }
        public Builder setPrice(double price){
            this.price = price;
            return this;}
        public Builder setStatus(Status status){
            this.status = status;
            return this;
        }
        public Builder copy(Property property){
            if (property.propertyID != null) {
                this.propertyID = property.propertyID;
            }
            this.propertyName = property.propertyName;
            this.numberOfRooms = property.numberOfRooms;
            this.address = property.address;
            this.price = property.price;
            this.pictures = property.pictures;
            this.landlord = property.landlord;
            this.status = property.status;

            return this;
        }

        public Property build(){
            return new Property(this);

        }

    }
}
