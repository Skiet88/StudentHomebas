package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    private static Student student1;
    private static Student student2;
    private static Student student3;
    private static Address address1;
    private static Address address2;
    private static Address address3;
    private static Contact contact1;
    private static Contact contact2;
    private static Contact contact3;
    private static AcademicDetails academicDetails1;
    private static AcademicDetails academicDetails2;
    private static AcademicDetails academicDetails3;
    private static byte[] file1;
    private static byte[] file2;
    private static byte[] file3;
    private static Document document1;
    private static Document document2;
    private static Document document3;
    private static List<Document> studentDocuments1;
    private static List<Document> studentDocuments2;
    private static List<Document> studentDocuments3;



    @BeforeAll
    static void setUp() {

        file1 = new byte[0];
        file2 = new byte[0];
        file3=new byte[0];

        document1 = DocumentFactory.buildDocument(1L, "MpumziMbulaDocument.pdf", file1, LocalDateTime.of(LocalDate.of(2024, 03, 24), LocalTime.of(14, 22)));
        document2 = DocumentFactory.buildDocument(2L, "Transcript.pdf", file2, LocalDateTime.of(LocalDate.of(2024, 04, 11), LocalTime.of(11, 14)));
        document3=DocumentFactory.buildDocument(3L,"AcademicRecord.pdf",file3,LocalDateTime.of(LocalDate.of(2024, 01, 11), LocalTime.of(19, 17)));

        studentDocuments1 = new ArrayList<>();
        studentDocuments1.add(document1);

        studentDocuments2 = new ArrayList<>();
        studentDocuments2.add(document2);

        studentDocuments3 = new ArrayList<>();
        studentDocuments3.add(document3);

        //Student 1
        address1 = AddressFactory.buildAddress("Ny 6 No 106", "Gugulethu", "Cape Town", "Cape Town");
        contact1 = new Contact.Builder().setAddress(address1).setEmail("mphumzimbula@gmail.com").setPhoneNumber("0658436358").build();
        academicDetails1 = new AcademicDetails.Builder().setAcademicDetailsID("1").setInstituteName("CPUT").setYearOfStudy(3).setProgramOfStudy("DICT:Application Development").build();

        student1 = StudentFactory.buildStudent(1L, "Mpumzi", "Mbula", "Male", LocalDate.of(2000, 06, 20), "m@123", academicDetails1, contact1, studentDocuments1);

        //Student 2
        address2 = AddressFactory.buildAddress("7556 Jan Smart", "BrackenFell", "Cape Town", "7750");
        contact2 = new Contact.Builder().setAddress(address2).setEmail("kaileymansoon@gmail.com").setPhoneNumber("0756355778").build();
        academicDetails2 = new AcademicDetails.Builder().setAcademicDetailsID("2").setInstituteName("CPUT").setYearOfStudy(3).setProgramOfStudy("DICT:Application Development").build();
        student2 = StudentFactory.buildStudent(4L, "Kailey", "Mansoon", "Zhubhear", "Male", LocalDate.of(2002, 05, 17), "k@123", academicDetails2, contact2, studentDocuments2);

        //Student 3
        address3 = AddressFactory.buildAddress("20267 Ben Moloise", "Khayelitsha", "Cape Town", "7784");
        contact3 = new Contact.Builder().setAddress(address3).setEmail("vxayiya@gmail.com").setPhoneNumber("0756356578").build();
        academicDetails3 = new AcademicDetails.Builder().setAcademicDetailsID("3").setInstituteName("CPUT").setYearOfStudy(1).setProgramOfStudy("DT:Diploma in Tourism").build();
        student3 = StudentFactory.buildStudent(5L, "Vuyokazi", "Joy", "Xayiya", "Male", LocalDate.of(2001, 02, 13), "v@123", academicDetails3, contact3, studentDocuments3);


    }

    @Test
    @Order(1)
    void save() {
        Student createdStudent1=studentService.save(student1);
        assertNotNull(createdStudent1);
        System.out.println(createdStudent1);

        Student createdStudent2=studentService.save(student2);
        assertNotNull(createdStudent2);
        System.out.println(createdStudent2);

        Student createdStudent3=studentService.save(student3);
        assertNotNull(createdStudent3);
        System.out.println(createdStudent3);


    }

    @Test
    @Order(2)
    void read() {

        Student studentRead=studentService.read(student2.getUserId());
        assertNotNull(studentRead);
        System.out.println( studentRead);
    }

    @Test
    @Order(3)
    void update() {
        Student studentToBeUpdated=new Student.StudentBuilder().copy(student2).setPassword("VXayiya@2027!").build();

        Student studentUpdated=studentService.update(studentToBeUpdated);
        assertNotNull(studentUpdated);
        System.out.println(studentUpdated);
    }
    @Test
    @Order(4)
    void authentication() {
        Student authenticatedStudent1 = studentService.authenticationByEmail("mphumzimbula@gmail.com", "20Mphmbu16!");
        assertNotNull(authenticatedStudent1);
        //assertEquals(student1.getUserId(), authenticatedStudent1.getUserId());
        System.out.println("Authenticated: " + authenticatedStudent1);

//        Student authenticatedStudent2 = studentService.authenticationByEmail("kaileymansoon@gmail.com", "Mpu@2022!!");
//        assertNotNull(authenticatedStudent1);
//        assertEquals(student1.getUserId(), authenticatedStudent1.getUserId());
//        System.out.println("Authenticated: " + authenticatedStudent1);
    }

    @Test
    @Order(5)
    void deleteById() {
        boolean isDeleted=studentService.deleteById(3L);
        assertTrue(isDeleted);
        System.out.println(student3.getUserId()+ " deleted Successfully");
    }

    @Test
    @Order(6)
    void getall() {
        List<Student>studentList=studentService.getall();
        assertNotNull(studentList);
        System.out.println(studentList);
    }

}