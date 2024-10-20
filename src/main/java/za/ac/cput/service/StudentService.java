package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.repository.ContactRepository;
import za.ac.cput.repository.DocumentRepository;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.StudentRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;
    private final ContactRepository contactRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private DocumentRepository documentRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {

        documentRepository.saveAll(student.getDocuments());
        contactRepository.save(student.getContact());

        String encodedPassword = passwordEncoder.encode(student.getPassword());
        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_STUDENT"));

        Student student2 = new Student.StudentBuilder()
                .copy(student)
                .setPassword(encodedPassword)
                .setRoles(Collections.singleton(studentRole))
                .build();

        return studentRepository.save(student2);
    }



    @Override
    public Student read(Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public Student update(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public boolean deleteById(Long studentId) {

        studentRepository.deleteById(studentId);
        return !studentRepository.existsById(studentId);
    }

    @Override
    public List<Student> getall() {
        return studentRepository.findAll();
    }

    public Student authenticationByEmail(String email, String password) {
        return studentRepository.findByContactEmailAndPassword(email, password);
    }

    public Student authenticate(Long id, String password) {
        Student std = studentRepository.findById(id).orElse(null);
        if (std != null && password.equals(std.getPassword())) {
            return std;
        }
        return null;
    }

}
