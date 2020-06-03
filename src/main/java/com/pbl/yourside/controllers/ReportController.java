package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.*;
import com.pbl.yourside.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/reports")
public class ReportController {

    private ReportRepository reportRepo;

    @Autowired
    public ReportController(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
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
        if (flag != null) {
            List<Report> reports = reportRepo.findAll();
            if (resolved) {
                reports = reports.stream().filter(report -> report.getStatus() == Status.RESOLVED).collect(Collectors.toList());
            } else {
                reports = reports.stream().filter(report -> report.getStatus() != Status.RESOLVED).collect(Collectors.toList());
            }
            if (flag.equalsIgnoreCase("teacher")) {
                reports = reports.stream().filter(report ->
                        report.getUsers().stream().map(User::getRole).map(Role::getName).collect(Collectors.toList()).contains(RoleName.ROLE_TEACHER)
                ).collect(Collectors.toList());
            } else {
                reports = reports.stream().filter(report ->
                        report.getUsers().stream().map(User::getRole).map(Role::getName).collect(Collectors.toList()).contains(RoleName.ROLE_STUDENT)
                ).collect(Collectors.toList());
            }
            return reports;
        }
        return reportRepo.findAll();
    }


    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<Report> updateReport(@RequestBody Report report, @PathParam("id") long id) {
        report.setId(id);
        reportRepo.save(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
