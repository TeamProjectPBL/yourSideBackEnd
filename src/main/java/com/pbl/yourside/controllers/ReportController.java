package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.*;
import com.pbl.yourside.repositories.ReportRepository;
import com.pbl.yourside.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/reports")
public class ReportController {

    private ReportRepository reportRepo;

    private final UserRepository userRepository;

    @Autowired
    public ReportController(ReportRepository reportRepo, UserRepository userRepository) {
        this.reportRepo = reportRepo;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public Report findById(@PathVariable("id") long id) {
        Report report = reportRepo.findById(id);
        if (report == null) {
            System.out.println("Report not found");
            return null;
        }
        return report;
    }

    @GetMapping
    public List<Report> findReports(@RequestParam(name = "id", required = false) Long id,
                                    @RequestParam(name = "flag", required = false) String flag,
                                    @RequestParam(name = "resolved", required = false) boolean resolved) {
        if (id != null) {
            List<Report> reports = reportRepo.findAll().stream().filter(report -> report.getTeacher().getId() == id || report.getStudent().getId() == id).collect(Collectors.toList());
            if (resolved) {
                reports = reports.stream().filter(report -> (report.getStatus() == Status.RESOLVED) || (report.getStatus() == Status.RATED)).collect(Collectors.toList());
            } else {
                reports = reports.stream().filter(report -> (report.getStatus() != Status.RESOLVED) && (report.getStatus() != Status.RATED)).collect(Collectors.toList());
            }
            return reports;
        }
        return reportRepo.findAll();
    }


    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
        report.setStatus(Status.UNREAD);

        report.setTeacher(userRepository.findById(report.getTeacher().getId()));
        //TODO: should all the ratings be initialized here?

        reportRepo.save(report);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Report> deleteAllReports() {
        reportRepo.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(@PathParam("id") long id) {
        Report report = reportRepo.findById(id);
        if (report == null) {
            System.out.println("Report not found");
            return null;
        }
        reportRepo.delete(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Report> replaceReports(@RequestBody List<Report> newReports) {
        reportRepo.deleteAll();
        for (Report report : newReports) {
            reportRepo.save(report);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Report> partialUpdate(@RequestBody Map<String, Object> updates, @PathVariable("id") long id) {
        Report report = this.reportRepo.findById(id);
        if (report == null) {
            System.out.println("Report not found");
            return new ResponseEntity<Report>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(report, updates);
        System.out.println(updates);
//        report.setStatus(Status.RATED);
        return new ResponseEntity<Report>(HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(Report report, Map<String, Object> updates) {
        if (updates.containsKey("status")) {
//            report.setStatus(Status.valueOf((String) updates.get("status")));
            String newStatus = (String) updates.get("status");
//            System.out.println(newStatus);
            switch(newStatus) {
                case "RATED":
                    report.setStatus(Status.RATED);
                    break;
                case "INPROGRESS":
                    report.setStatus(Status.INPROGRESS);
                    break;
                case "RESOLVED":
                    report.setStatus(Status.RESOLVED);
                    break;
                case "UNRESOLVABLE":
                    report.setStatus(Status.UNRESOLVABLE);
                    break;
                case "READ":
                    report.setStatus(Status.READ);
                    break;
            }
        }
        if (updates.containsKey("commit")) {
            report.setCommit((int) updates.get("commit"));
        }
        if (updates.containsKey("resolution")) {
            report.setResolution((int) updates.get("resolution"));
        }
        if (updates.containsKey("contact")) {
            report.setContact((int) updates.get("contact"));
        }
        if (updates.containsKey("speed")) {
            report.setSpeed((int) updates.get("speed"));
        }
        if (updates.containsKey("comments")) {
            report.setComments((String) updates.get("comments"));
        }
        if (updates.containsKey("resolved")) {
            report.setReviewed((Boolean) updates.get("resolved"));
        }
        this.reportRepo.save(report);
    }

}
