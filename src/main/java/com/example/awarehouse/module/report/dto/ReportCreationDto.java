package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportInterval;
import jakarta.validation.constraints.Email;

import java.util.UUID;


public record ReportCreationDto (UUID reportScopeId, ReportScope reportScope, ReportInterval reportInterval,
                                 @Email(message = "Invalid email format")
                                 String email){
}
