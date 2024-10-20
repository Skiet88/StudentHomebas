//package za.ac.cput.factory;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import za.ac.cput.domain.Contact;
//import za.ac.cput.domain.Name;
//import za.ac.cput.domain.User;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class UserFactoryTest {
//    private User user1;
//    private User user2;
//
//    private User user3;
//
//    @BeforeEach
//    void setUp(){
//        Name name1 = new Name.NameBuilder().setFirstName("Sipho")
//                .setMiddleName("Sethu")
//                .setLastName("Makhambeni")
//                .build();
//        Name name2 = new Name.NameBuilder().setFirstName("William")
//                .setMiddleName("Siips")
//                .setLastName("Belic")
//                .build();
//        Name name3 = new Name.NameBuilder().setFirstName("Franklin")
//                .setMiddleName("Andre")
//                .setLastName("Clinton")
//                .build();
//
//        Contact contact1 = new Contact.Builder()
//                .setEmail("Makhambenisethu@gmail.com")
//                .setPhoneNumber("0782126539")
//                .build();
//        Contact contact2 = new Contact.Builder()
//                .setEmail("WiSiBE@gmail.com")
//                .setPhoneNumber("0713146034")
//                .build();
//        Contact contact3 = new Contact.Builder()
//                .setEmail("fc11gtav@gmail.com")
//                .setPhoneNumber("0657728901")
//                .build();
//
//
//        user1 = UserFactory.buildUser(1L,name1,"Male",LocalDate.of(2000,07,02),"password",contact1);
//        user2 = UserFactory.buildUser(2L,name2,"Female",LocalDate.of(2002,02,05),"1L0v3u",contact2);
//        user3 = UserFactory.buildUser(3L,name1,"Male",LocalDate.of(1990,01,22),"securepassword",contact3);
//
//
//
//    }
//    @Test
//    void buildUserWithValidDetails(){
//        assertNotNull(user1);
//        System.out.println(user1);
//    }
//
//    @Test
//    void buildUserBornBefore2000(){
//        assertNotNull(user3);
//        System.out.println(user3);
//    }
//    @Test
//    void buildUserWithUniquePasswordMustFail(){
//        assertNull(user2);
//        System.out.println(user2);
//    }
//}
