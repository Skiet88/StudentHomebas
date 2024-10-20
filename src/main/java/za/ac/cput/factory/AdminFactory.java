package za.ac.cput.factory;

import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Name;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class AdminFactory {
    public static Admin buildAdmin(long adminId, String firstName, String middleName, String lastName, String gender,
                                   LocalDate dateOfBirth, String password, Contact contact) {
        if (adminId <= 0 || Helper.isNullOrEmpty(firstName)
                || Helper.isNullOrEmpty(middleName)
                || Helper.isNullOrEmpty(lastName)
                || Helper.isNullOrEmpty(gender)
                || Helper.isInvalidDate(dateOfBirth)
                || Helper.isNullOrEmpty(password)
                || Helper.isObjectNull(contact)) {
            return null;
        }

        Name name = new Name.NameBuilder()
                .setFirstName(firstName)
                .setMiddleName(middleName)
                .setLastName(lastName)
                .build();

        return new Admin.AdminBuilder()
                .setUserId(adminId)
                .setName(name)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setPassword(password)
                .setContact(contact)
                .build();
    }

    public static Admin buildAdmin(long adminId, String firstName, String lastName, String gender,
                                   LocalDate dateOfBirth, String password, Contact contact) {
        if (adminId <= 0 || Helper.isNullOrEmpty(firstName)
                || Helper.isNullOrEmpty(lastName)
                || Helper.isNullOrEmpty(gender)
                || Helper.isInvalidDate(dateOfBirth)
                || Helper.isNullOrEmpty(password)
                || Helper.isObjectNull(contact)) {
            return null;
        }

        Name name = new Name.NameBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();

        return new Admin.AdminBuilder()
                .setUserId(adminId)
                .setName(name)
                .setGender(gender)
                .setDateOfBirth(dateOfBirth)
                .setPassword(password)
                .setContact(contact)
                .build();
    }
}
