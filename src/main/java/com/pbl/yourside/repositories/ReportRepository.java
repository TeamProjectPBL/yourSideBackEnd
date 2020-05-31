package com.pbl.yourside.repositories;

import com.pbl.yourside.entities.Report;
import com.pbl.yourside.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBytId(long tId);

    List<Report> findBysId(long sId);

    List<Report> findAll();

    @Query("select r from Report r where r.status = ?1 and r.tId = ?2")
    List<Report> findByStatusAndTId(Status status, long tId);

    @Query("select r from Report r where r.status = ?1 and r.sId = ?2")
    List<Report> findByStatusAndSId(Status status, long sId);

    @Query("select r from Report r where r.status <> ?1 and r.tId = ?2")
    List<Report> findByNotResolvedStatusAndTId(Status status, long tId);

    @Query("select r from Report r where r.status <> ?1 and r.sId = ?2")
    List<Report> findByNotResolvedStatusAndSId(Status status, long sId);

    Report findById(long id);
}
