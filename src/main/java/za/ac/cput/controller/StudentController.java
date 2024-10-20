package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Student;
import za.ac.cput.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    public Student save(@RequestBody Student student) {
        return studentService.save(student);
    }

    @GetMapping("/read/{studentId}")
    public Student read(@PathVariable("studentId") Long studentId) {
        return studentService.read(studentId);
    }

    @PostMapping("/update")
    public Student update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @DeleteMapping("/delete/{studentId}")
    public boolean delete(@PathVariable("studentId") Long studentId) {
        return studentService.deleteById(studentId);
    }

    @GetMapping("/getall")
    public List<Student> getall() {
        return studentService.getall();
    }

    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<String> login(@PathVariable("email") String email, @PathVariable("password") String password) {

        if (email ==null || email.isEmpty()) {
            return new ResponseEntity<>("email is null" + email, HttpStatus.BAD_REQUEST);
        }

        Student authenticatedStudent = studentService.authenticationByEmail(email, password);

        if (authenticatedStudent != null) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

}

