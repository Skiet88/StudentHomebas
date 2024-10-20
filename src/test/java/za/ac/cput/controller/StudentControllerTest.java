package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import za.ac.cput.DTO.AuthenticationRequest;
import za.ac.cput.domain.*;
import za.ac.cput.factory.AddressFactory;
import za.ac.cput.factory.DocumentFactory;
import za.ac.cput.factory.StudentFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String BASE_URL = "/student";

    private static Student student1;
    private static Student student2;

    private static Address address1;
    private static Address address2;
    private static Contact contact1;
    private static Contact contact2;
    private static AcademicDetails academicDetails1;
    private static AcademicDetails academicDetails2;
    private static byte[] file1;
    private static byte[] file2;

    private static Document document1;
    private static Document document2;

    private static List<Document> studentDocuments1;
    private static List<Document> studentDocuments2;

    @BeforeAll
    static void setUp() {
        file1 = new byte[0];
        file2 = new byte[0];

        document1 = DocumentFactory.buildDocument(1L, "MpumziMbulaDocument.pdf", file1, LocalDateTime.of(LocalDate.of(2024, 03, 24), LocalTime.of(14, 22)));
        document2 = DocumentFactory.buildDocument(2L, "Transcript.pdf", file2, LocalDateTime.of(LocalDate.of(2024, 04, 11), LocalTime.of(11, 14)));

        studentDocuments1 = new ArrayList<>();
        studentDocuments1.add(document1);

        studentDocuments2 = new ArrayList<>();
        studentDocuments2.add(document2);

        // Student 1
        address1 = AddressFactory.buildAddress("Ny 6 No 106", "Gugulethu", "Cape Town", "Cape Town");
        contact1 = new Contact.Builder().setAddress(address1).setEmail("mphumzimbula@gmail.com").setPhoneNumber("0658436358").build();
        academicDetails1 = new AcademicDetails.Builder().setAcademicDetailsID("1").setInstituteName("CPUT").setYearOfStudy(3).setProgramOfStudy("DICT:Application Development").build();
        student1 = StudentFactory.buildStudent(1L, "Mpumzi", "Mbula", "Male", LocalDate.of(2000, 06, 20), "20Mphmbu16!", academicDetails1, contact1, studentDocuments1);

        // Student 2
        address2 = AddressFactory.buildAddress("7556 Jan Smart", "BrackenFell", "Cape Town", "7750");
        contact2 = new Contact.Builder().setAddress(address2).setEmail("kaileymansoon@gmail.com").setPhoneNumber("0756355778").build();
        academicDetails2 = new AcademicDetails.Builder().setAcademicDetailsID("2").setInstituteName("CPUT").setYearOfStudy(3).setProgramOfStudy("DICT:Application Development").build();
        student2 = StudentFactory.buildStudent(2L, "Kailey", "Mansoon", "Zhubhear", "Male", LocalDate.of(2002, 05, 17), "Mpu@2022!!", academicDetails2, contact2, studentDocuments2);
    }

    @Test
    @Order(1)
    void save() {
        String url = BASE_URL + "/save";
        ResponseEntity<Student> postResponse1 = testRestTemplate.postForEntity(url, student1, Student.class);
        assertNotNull(postResponse1);
        assertNotNull(postResponse1.getBody());
        System.out.println("Saved Student 1:" + postResponse1.getBody());

        ResponseEntity<Student> postResponse2 = testRestTemplate.postForEntity(url, student2, Student.class);
        assertNotNull(postResponse2);
        assertNotNull(postResponse2.getBody());
        System.out.println("Saved Student 2:" + postResponse2.getBody());
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL + "/read/" + student1.getUserId();
        ResponseEntity<Student> response = testRestTemplate.getForEntity(url, Student.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        System.out.println("Read Student:" + response.getBody());
    }

    @Test
    @Order(3)
    void update() {
        String url = BASE_URL + "/update";
        Student updatedStudent = new Student.StudentBuilder().copy(student1).setPassword("17*08ManDown").build();
        ResponseEntity<Student> response = testRestTemplate.postForEntity(url, updatedStudent, Student.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(updatedStudent.getUserId(), response.getBody().getUserId());
        System.out.println("Updated Student:" + response.getBody());
    }

    @Test
    @Order(4)
    void delete() {
        String url = BASE_URL + "/delete/" + student2.getUserId();
        testRestTemplate.delete(url);
        System.out.println("Student with userId " + student2.getUserId() + " Deleted successfully");
    }

    @Test
    @Order(5)
    void getall() {
        String url = BASE_URL + "/getall";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertNotNull(response);
        System.out.println("All Students");
        System.out.println(response.getBody());
    }

    @Test
    @Order(6)
    void login() {
        String url = BASE_URL + "/login";
        Student loginRequest = new Student.StudentBuilder().copy(student1).build(); // Using student1's email and password for login
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(url, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
        assertEquals("Login successful", response.getBody());
        System.out.println("Login Test Response: " + response.getBody());
    }


//    @Test
//    @Order(6)
//    void authenticate_success() {
//        String url = BASE_URL + "/authenticate";
//        AuthenticationRequest request = new AuthenticationRequest("mphumzimbula@gmail.com", "20Mphmbu16!");
//        ResponseEntity<Student> response = testRestTemplate.postForEntity(url, request, Student.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(student1.getUserId(), response.getBody().getUserId());
//        System.out.println("Authentication Successful for Student 1:" + response.getBody());
//    }


}
