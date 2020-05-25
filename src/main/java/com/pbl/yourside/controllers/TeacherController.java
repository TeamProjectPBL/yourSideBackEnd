package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Teacher;
import com.pbl.yourside.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/teachers")
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    public TeacherController(TeacherRepository teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    @GetMapping
    public List<Teacher> findAll() {
        return teacherRepo.findAll();
    }

    @GetMapping("/{id}")
    public Teacher findById(@PathParam("id") long id) {
        Teacher teacher = teacherRepo.findById(id);
        if (teacher == null) {
            System.out.println("Teacher not found");
            return null;
        }
        return teacher;
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        teacherRepo.save(teacher);
        return new ResponseEntity<Teacher>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Teacher> deleteAllTeachers() {
        teacherRepo.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathParam("id") long id) {
        Teacher teacher = teacherRepo.findById(id);
        if (teacher == null) {
            System.out.println("Teacher not found");
            return null;
        }
        teacherRepo.delete(teacher);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Teacher> replaceTeachers(@RequestBody List<Teacher> newTeachers) {
        teacherRepo.deleteAll();
        for (Teacher teacher : newTeachers) {
            teacherRepo.save(teacher);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@RequestBody Teacher teacher, @PathParam("id") long id) {
        teacher.setId(id);
        teacherRepo.save(teacher);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
