package za.ac.cput.service.landlordService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.*;
import za.ac.cput.factory.AddressFactory;
import za.ac.cput.factory.ContactFactory;
import za.ac.cput.factory.DocumentFactory;
import za.ac.cput.factory.LandlordFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.class)
class LandlordServiceTest {

    @Autowired
    private LandlordService landlordService;
    private Landlord landlord1;
    private Landlord landlord2;
    List<Document> documentList = new ArrayList<>();
    List<Document> documentList2 = new ArrayList<>();
    private static byte[] file;

    @BeforeEach
    void setUp() {
        file=new byte[0];
        Document document1 = DocumentFactory.buildDocument(1L,"MikeSeptemeberCopyOfID",file, LocalDateTime.of(LocalDate.of(2024,03,24), LocalTime.of(14,22)));
        Document document2 = DocumentFactory.buildDocument(2L, "MpumziMbulaDocument.pdf", file, LocalDateTime.of(LocalDate.of(2024, 03, 24), LocalTime.of(14, 22)));
        System.out.println(document1);
        documentList.add(document1);
        documentList2.add(document2);

        Address address1= AddressFactory.buildAddress("9 Lower Street", "Mowbray", "Cape Town", "5100");
        Contact contact = ContactFactory.createContact("0786549009", "mikes@gmail.com", address1);
        landlord1 = LandlordFactory.buildLandlordWithMiddleName("Mike", "Matic", "September", "Male", LocalDate.of(1986,8,13), 3, "m@123",contact,documentList);

        Address address2 = AddressFactory.buildAddress("19 Lower Street", "Mowbray", "Cape Town", "5100");
        Contact contact2 = ContactFactory.createContact("0786548790", "nickola@gmail.com", address2);
        landlord2 = LandlordFactory.buildLandlordWithMiddleName("Nick", "Leon", "September", "Male", LocalDate.of(1986,8,14), 2, "n@123",contact2,documentList2);


    }

    @Test
    void create() {
        System.out.println("===========================CREATE========================================");
        landlord1.getDocuments();
        Landlord savedLandlord = landlordService.save(landlord1);
        assertNotNull(savedLandlord);
        System.out.println(savedLandlord);

        Landlord savedLandlord2 = landlordService.save(landlord2);
        assertNotNull(savedLandlord2);
        System.out.println(savedLandlord2);
    }

    @Test
    @Order(2)
    void read() {
        Landlord readLandlord = landlordService.read(landlord1.getUserId());
        assertNotNull(readLandlord);
        System.out.println(readLandlord);
    }

    @Test
    @Order(3)
    void update() {
        System.out.println("========================================UPDATE================================================");
        Landlord updatedLandlord = new Landlord.LandlordBuilder().copy(landlord2)
                .setPassword("Matic13099")
                .build();
        Landlord savedLandlord = landlordService.save(updatedLandlord);
        assertNotNull(savedLandlord);
        System.out.println(savedLandlord);
    }

    @Test
    @Order(4)
    void deleteById() {
        boolean deleted = landlordService.deleteById(landlord1.getUserId());
        assertTrue(deleted);
        System.out.println("Address deleted");
    }

    @Test
    @Order(5)
    void getall() {
        List<Landlord> landlordList = landlordService.getall();
        assertNotNull(landlordList);
        System.out.println(landlordList);
    }
}