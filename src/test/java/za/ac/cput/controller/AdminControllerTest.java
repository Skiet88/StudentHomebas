package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Admin;
import za.ac.cput.domain.Contact;
import za.ac.cput.factory.AddressFactory;
import za.ac.cput.factory.AdminFactory;
import za.ac.cput.factory.ContactFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    final String BASE_URL = "http://localhost:8080/StudentHomeBas/admin";
    private Admin admin1;
    private Admin admin2;
    private Admin admin3;

    @BeforeEach
    void a_setUp() {
        Address address1 = AddressFactory.buildAddress("9 Lower Street", "Mowbray", "Cape Town", "5100");
        Address address2 = AddressFactory.buildAddress("15 Upper Road", "Gardens", "Cape Town", "8001");
        Address address3 = AddressFactory.buildAddress("22 Main Street", "Claremont", "Cape Town", "7700");

        Contact contact1 = ContactFactory.createContact("083456789", "leago@gmail.com", address1);
        Contact contact2 = ContactFactory.createContact("076654321", "rachel@gmail.com", address2);
        Contact contact3 = ContactFactory.createContact("065666777", "john@gmail.com", address3);

        admin1 = AdminFactory.buildAdmin(1, "Leago", "Michael", "Modise",
                "Female", LocalDate.of(1990, 5, 15), "leagod123", contact1);

        admin2 = AdminFactory.buildAdmin(2, "Rachel", "Tidi", "Moloi", "Female",
                LocalDate.of(1995, 12, 5), "femeo156", contact2);

        admin3 = AdminFactory.buildAdmin(3, "John", "Bonele", "Mona", "Male",
                LocalDate.of(1982, 2, 13), "Mona@1982", contact3);
    }

    @Test
    @Order(1)
    void b_create() {
        String url = BASE_URL + "/save";

        System.out.println(admin1);
        ResponseEntity<Admin> postResponse1 = restTemplate.postForEntity(url, admin1, Admin.class);

        assertNotNull(postResponse1);
        assertNotNull(postResponse1.getBody());
        System.out.println("Saved Contact:" + postResponse1.getBody());

        ResponseEntity<Admin> postResponse2 = restTemplate.postForEntity(url, admin2, Admin.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Saved Admin: " + postResponse2.getBody());

        ResponseEntity<Admin> postResponse3 = restTemplate.postForEntity(url, admin3, Admin.class);
        assertNotNull(postResponse3);
        assertNotNull(postResponse3.getBody());
        System.out.println("Saved Admin: " + postResponse3.getBody());
        ResponseEntity<Admin> postResponse4 = restTemplate.postForEntity(url, admin3, Admin.class);
        assertNotNull(postResponse4);
        assertNotNull(postResponse4.getBody());
        System.out.println("Saved Admin: " + postResponse4.getBody());
    }

    @Test
    @Order(2)
    void c_read() {
        String url = BASE_URL + "/read/" + admin1.getUserId();

        System.out.println(admin1.getUserId());

        ResponseEntity<Admin> response = restTemplate.getForEntity(url, Admin.class);
        assertNotNull(response);
        Admin admin = response.getBody();
        assertNotNull(admin);
        assertEquals(admin.getUserId(), admin1.getUserId());
        System.out.println("Read Admin: " + admin);
    }

    @Test
    @Order(3)
    void d_update() {
        String url = BASE_URL + "/update";

        Admin updatedAdmin = new Admin.AdminBuilder()
                .copy(admin2)
                .setPassword("newPassword123")
                .build();
        ResponseEntity<Admin> postResponse = restTemplate.postForEntity(url, updatedAdmin, Admin.class);
        assertNotNull(postResponse);
        Admin admin = postResponse.getBody();
        assertNotNull(admin);
        assertEquals(admin.getUserId(), admin2.getUserId());
        System.out.println("Updated Admin: " + admin);
    }

    @Test
    @Order(4)
    //@Disabled
    void e_delete() {
        String url = BASE_URL + "/delete/" + admin1.getUserId();

       restTemplate.delete(url);
       System.out.println("Deleted Admin: " + admin1);
    }

    @Test
    @Order(5)
    void f_getAll() {
        String url = BASE_URL + "/getAll";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertNotNull(response.getBody());
        System.out.println("Admins found:" + response.getBody());
    }
}
