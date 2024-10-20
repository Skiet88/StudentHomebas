package za.ac.cput.factory;

import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Document;
import za.ac.cput.domain.Landlord;
import za.ac.cput.domain.Name;
import za.ac.cput.util.Helper;

import java.time.LocalDate;
import java.util.List;

public class LandlordFactory {
    public static Landlord buildLandlord(Long landlordId, String firstName,String lastName, int numOfPropertiesOwned, String gender, LocalDate dateOfBirth, String password, Contact contact, List<Document> documents){
    if(landlordId<=0 || Helper.isNullOrEmpty(firstName)|| Helper.isNullOrEmpty(lastName)
    || numOfPropertiesOwned<=0 || Helper.isNullOrEmpty(gender) || Helper.isInvalidDate(dateOfBirth) || Helper.isNullOrEmpty(password) || contact == null
    || Helper.isListNullorEmpty(documents)){
        return null;
    }
        Name name = new Name.NameBuilder().setFirstName(firstName)
                .setLastName(lastName)
                .build();
    return new Landlord.LandlordBuilder()
            .setUserId(landlordId)
            .setName(name)
            .setGender(gender)
            .setDateOfBirth(dateOfBirth)
            .setNumOfPropertiesOwned(numOfPropertiesOwned)
            .setPassword(password)
            .setContact(contact)
            .setDocuments(documents)
            .build();

    }
    public static Landlord buildLandlordWithMiddleName(Long landlordId, String firstName, String middleName, String lastName, String gender, LocalDate dateOfBirth, int numOfPropertiesOwned, String password, Contact contact, List<Document> documents){
        if(landlordId<=0 || Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(middleName) || Helper.isNullOrEmpty(lastName)
                || numOfPropertiesOwned<=0 || Helper.isNullOrEmpty(gender) || Helper.isInvalidDate(dateOfBirth) || Helper.isNullOrEmpty(password) || contact == null
                || Helper.isListNullorEmpty(documents)){
            return null;
        }
        Name name = new Name.NameBuilder().setFirstName(firstName)
                .setLastName(lastName)
                .setMiddleName(middleName)
                .build();
        return new Landlord.LandlordBuilder()
                .setUserId(landlordId)
                .setName(name)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setNumOfPropertiesOwned(numOfPropertiesOwned)
                .setPassword(password)
                .setContact(contact)
                .setDocuments(documents)
                .build();

    }

    public static Landlord buildLandlordWithMiddleName( String firstName, String middleName, String lastName, String gender, LocalDate dateOfBirth, int numOfPropertiesOwned, String password, Contact contact, List<Document> documents){
        if( Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(middleName) || Helper.isNullOrEmpty(lastName)
                || numOfPropertiesOwned<=0 || Helper.isNullOrEmpty(gender) || Helper.isInvalidDate(dateOfBirth) || Helper.isNullOrEmpty(password) || contact == null
                || Helper.isListNullorEmpty(documents)){
            return null;
        }
        Name name = new Name.NameBuilder().setFirstName(firstName)
                .setLastName(lastName)
                .setMiddleName(middleName)
                .build();
        return new Landlord.LandlordBuilder()
                .setName(name)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setNumOfPropertiesOwned(numOfPropertiesOwned)
                .setPassword(password)
                .setContact(contact)
                .setDocuments(documents)
                .build();

    }


}