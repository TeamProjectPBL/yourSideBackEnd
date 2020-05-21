package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Report;
import com.pbl.yourside.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/reports")
public class ReportController {
    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    public ReportController(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    @GetMapping
    public List<Report> findAllReports() {return reportRepo.findAll();}

    @GetMapping("/{id}")
    public Report findById(@PathVariable("id") long id) {
        Report report = reportRepo.findById(id);
        if (report==null) {
            System.out.println("Report not found");
            return null;
        }
        return report;
    }

    @GetMapping
    public List<Report> findByTeacher(@RequestParam(name="tid") long tId) {
        return reportRepo.findByTeacher(tId);
    }

    @GetMapping
    public List<Report> findByStudent(@RequestParam(name="sid") long sId) {
        return reportRepo.findByStudent(sId);
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
        if (report==null) {
            System.out.println("Report not found");
            return null;
        }
        reportRepo.delete(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Report> replaceReports(@RequestBody List<Report> newReports) {
        reportRepo.deleteAll();
        for (Report report:newReports) {
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
