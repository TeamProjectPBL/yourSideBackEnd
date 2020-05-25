package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Student;
import com.pbl.yourside.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable("id") long id) {
        Student student = studentRepo.findById(id);
        if (student == null) {
            System.out.println("Student not found");
            return null;
        }
        return student;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentRepo.save(student);
        return new ResponseEntity<Student>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Student> deleteAllStudents() {
        studentRepo.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathParam("id") long id) {
        Student student = studentRepo.findById(id);
        if (student == null) {
            System.out.println("Student not found");
            return null;
        }
        studentRepo.delete(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Student> replaceStudents(@RequestBody List<Student> newStudents) {
        studentRepo.deleteAll();
        for (Student student : newStudents) {
            studentRepo.save(student);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathParam("id") long id) {
        student.setId(id);
        studentRepo.save(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
