package za.ac.cput.service.AccountServiceTest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.*;
import za.ac.cput.factory.AccountFactory;
import za.ac.cput.service.AccountService.AccountService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.class)
public class AccountServiceTest {
   @Autowired
    private AccountService accountService;

    private Account account1;
    private Account account2;
    private Account account3;
    //

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
    void create(){
        System.out.println("======CREATE=======");
        Account createdAccount = accountService.save(account1);
        assertNotNull(createdAccount);
        System.out.println(createdAccount);

        
    }
    @Test
    @Order(2)
    void read(){
       Account readAccount = accountService.read(account1.getAccountId());
        System.out.println(account1.getAccountId());
        assertNotNull(readAccount);
        System.out.println(readAccount);

    }
    @Test
    @Order(3)
    void update(){
        System.out.println("======UPDATE======");

        Account updatedAccount = new Account.AccountBuilder().copy(account1)
                .setBalance(500.00)
                .build();
        Account savedAccount = accountService.save(updatedAccount);
        assertNotNull(savedAccount);
        System.out.println(savedAccount);
    }

    @Test
    @Order(4)
    void deleteById(){
        boolean deleted = accountService.deleteById(account1.getAccountId());
        assertTrue(deleted);
        System.out.println("Account Deleted");
    }

    @Test
    @Order(5)
    void getAll(){
        List<Account> accountList = accountService.getAll();
        assertNotNull(accountList);
        System.out.println(accountList);
    }
}
