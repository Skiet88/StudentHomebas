package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.*;
import za.ac.cput.factory.AccountFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    final String BASE_URL = "http://localhost:8080/StudentHomeBas/account";

    private Account account1;
    private Account account2;
    private Account account3;
    @BeforeEach
    void setUp() {
        Name name = new Name.NameBuilder().setFirstName("John").setMiddleName("Mean").setLastName("Doe").build();
        Contact contact = new Contact.Builder()
                .setEmail("Johndoe@gmail.com")
                .setPhoneNumber("07821265349")
                .build();

        Student user = new Student.StudentBuilder()
                .setUserId(1L)
                .setName(name)
                .setGender("Male")
                .setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setPassword("securepassword")
                .setContact(contact)
                .build();

        account1 = AccountFactory.buildAccount(12345L, "Checking", "VISA", 1000.00,
                LocalDate.of(2024, 7, 1), "Pending", "Active", user);

        account2 = AccountFactory.buildAccount(23456L, "Savings", "MASTERCARD", 2000.00,
                LocalDate.of(2023, 7, 1), "Paid", "Inactive", user);

        account3 = AccountFactory.buildAccount(30987L, "Checking", "VISA", -500.00,
                LocalDate.of(2024, 7, 1), "Pending", "Active", user);
    }
    @Test
    void b_create(){
        String url = BASE_URL + "/save";

        System.out.println(account1);
        ResponseEntity<Account> postResponse1 = restTemplate.postForEntity(url, account1, Account.class);

        assertNotNull(postResponse1);
        assertNotNull(postResponse1.getBody());
        System.out.println("Saved Account:"+ postResponse1.getBody());

        System.out.println(account2);
        ResponseEntity<Account> postResponse2 = restTemplate.postForEntity(url, account2, Account.class);

        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Saved Account:"+ postResponse2.getBody());

        System.out.println(account3);
        ResponseEntity<Account> postResponse3 = restTemplate.postForEntity(url, account3, Account.class);

        assertNotNull(postResponse3);
        assertNotNull(postResponse3.getBody());
        System.out.println("Saved Account:"+ postResponse3.getBody());

    }

    @Test
    @Order(2)
    void c_read(){
        String url = BASE_URL + "/read/" + account1.getAccountId();

        System.out.println(account1.getAccountId());

        ResponseEntity<Account> response = restTemplate.getForEntity(url, Account.class);
        assertNotNull(response);
        Account account = response.getBody();
        assertNotNull(account);
        assertEquals(account.getAccountId(),account1.getAccountId());
        System.out.println("Read Account: " + account);
    }

    @Test
    @Order(3)
    void d_update(){
        String url = BASE_URL + "/update";

        Account updatedAccount = new Account.AccountBuilder().copy(account2).setBalance(500.00).build();
        ResponseEntity<Account> postResponse = restTemplate.postForEntity(url, updatedAccount, Account.class);
        assertNotNull(postResponse);
        Account account = postResponse.getBody();
        assertNotNull(account);
        assertEquals(account.getAccountId(), account2.getAccountId());
        System.out.println("Updated account: " + account);
    }

    @Test
    @Order(4)
    void e_delete(){
        String url = BASE_URL + "/delete" + account3.getAccountId();

        restTemplate.delete(url);
        System.out.println("Deleted Account: " + account3);
    }

    @Test
    @Order(5)
    void f_getAll(){
        String url = BASE_URL + "/getAll";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertNotNull(response.getBody());
        System.out.println("Accounts found:" + response.getBody());
    }
}
