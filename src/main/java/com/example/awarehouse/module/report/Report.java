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

    private ReportType reportType;

    private String email;

    private ReportScope reportScope;

    private UUID scopeEntityId;

    private String token;

    public Report(ZonedDateTime nextGenerationDate, ReportType reportType, String email, ReportScope reportScope, UUID scopeEntityId) {
        this.nextGenerationDate = nextGenerationDate;
        this.reportType = reportType;
        this.email = email;
        this.reportScope = reportScope;
        this.scopeEntityId = scopeEntityId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
