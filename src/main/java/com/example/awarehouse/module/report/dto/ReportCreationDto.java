package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


public record ReportCreationDto (UUID id, ReportScope reportScope, ReportType reportType, String email){
}
