package com.example.awarehouse.module.report;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Inheritance
@NoArgsConstructor
@Getter
public class Report {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private ZonedDateTime nextGenerationDate;

    @Enumerated(EnumType.STRING)
    private ReportInterval reportInterval;

    private String email;

    private ReportScope reportScope;

    private UUID scopeEntityId;

    private String token;

    public Report(ZonedDateTime nextGenerationDate, ReportInterval reportInterval, String email, ReportScope reportScope, UUID scopeEntityId) {
        this.nextGenerationDate = nextGenerationDate;
        this.reportInterval = reportInterval;
        this.email = email;
        this.reportScope = reportScope;
        this.scopeEntityId = scopeEntityId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void adjustNextGenerationTime() {
       if(reportInterval.equals(ReportInterval.DAILY)){
           nextGenerationDate= nextGenerationDate.plusDays(1);
       } else if (reportInterval.equals(ReportInterval.WEEKLY)) {
           nextGenerationDate= nextGenerationDate.plusWeeks(1);
       } else if (reportInterval.equals(ReportInterval.MONTHLY)) {
           nextGenerationDate= nextGenerationDate.plusMonths(1);

       }
    }
}
