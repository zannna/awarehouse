package com.example.awarehouse.module.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    @Query(value = "SELECT * FROM Report r WHERE r.reportType = 'DAILY' OR " +
            "r.nextGenerationDate  AT TIME ZONE 'UTC' < : currentUTCTime", nativeQuery = true)
    public Page<Report> findToGenerate(ZonedDateTime currentUTCTime, Pageable pageable);
}
