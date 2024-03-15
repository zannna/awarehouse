package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportInterval;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record ReportCreationDto (
        @NotNull(message = "Report scope id cannot be null") UUID reportScopeId,
        @NotNull(message = "Report scope cannot be null") ReportScope reportScope,
        @NotNull(message = "Report interval cannot be null") ReportInterval reportInterval,
        @Email
        @NotBlank(message = "Email must not be empty")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address format")
        String email){
}
