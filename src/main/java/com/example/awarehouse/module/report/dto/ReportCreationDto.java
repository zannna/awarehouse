package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportInterval;

import java.util.UUID;


public record ReportCreationDto (UUID id, ReportScope reportScope, ReportInterval reportInterval, String email){
}
