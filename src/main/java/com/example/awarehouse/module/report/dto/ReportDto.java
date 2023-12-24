package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportInterval;

import java.util.UUID;

public record ReportDto(UUID id, ReportInterval reportInterval, String email, ReportScope reportScope, UUID reportScopeId) {
}
