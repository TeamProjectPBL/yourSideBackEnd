package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Report;
import com.pbl.yourside.entities.RoleName;
import com.pbl.yourside.entities.Status;
import com.pbl.yourside.entities.User;
import com.pbl.yourside.models.TeacherProfile;
import com.pbl.yourside.repositories.ReportRepository;
import com.pbl.yourside.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/users")
public class UserController {

    private final UserRepository userRepository;

    private final ReportRepository reportRepository;

    @Autowired
    public UserController(UserRepository userRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/{id}")
    private User findById(@PathVariable("id") long id){
        return userRepository.findById(id);
    }

    @GetMapping("/getCurrentUser")
    private User getCurrentUser(){
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(userLogin);
    }


    @GetMapping("/getTeachersProfiles")
    public List<TeacherProfile> getTeachersProfiles() {
        List<Long> ids = userRepository.findAll().stream().filter(user -> user.getRole().getName() == RoleName.ROLE_TEACHER).map(user -> user.getId()).collect(Collectors.toList());
        List<TeacherProfile> teacherProfiles = new LinkedList<>();
        for (long id : ids) {
            List<Report> reports = reportRepository.findAll().stream().filter(report -> report.getStatus() == Status.RATED).filter(report -> report.getTeacher().getId() == id).collect(Collectors.toList());
            TeacherProfile profile = new TeacherProfile();
            profile.setSurname(userRepository.findById(id).getLastName());
            profile.setName(userRepository.findById(id).getFirstName());
            if (reports.isEmpty()) {
                teacherProfiles.add(profile);
                continue;
            }
            profile.setReviewed(true);
            profile.setCommit((double)reports.stream().map(Report::getCommit).reduce(0, Integer::sum) / reports.size());
            profile.setContact((double)reports.stream().map(Report::getContact).reduce(0, Integer::sum) / reports.size());
            profile.setResolution((double)reports.stream().map(Report::getResolution).reduce(0, Integer::sum) / reports.size() );
            profile.setSpeed((double)reports.stream().map(Report::getSpeed).reduce(0, Integer::sum) / reports.size());
            teacherProfiles.add(profile);
        }
        return teacherProfiles;
    }

    @GetMapping("/getAllTeachers")
    private List<User> getAllTeachers() {
        return userRepository.findAll().stream().filter(user -> user.getRole().getName() == RoleName.ROLE_TEACHER).collect(Collectors.toList());

    }

    @GetMapping
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user){
        userRepository.save(user);
        return new ResponseEntity<User>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathParam("id") long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePartialUser(@RequestBody Map<String, Object> updates, @PathVariable("id") long id){
        User user = userRepository.findById(id);
        if(user == null){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(user, updates);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(User user, Map<String, Object> updates){

        //Add if we need to modify user in any way
    }

}


