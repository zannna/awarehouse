package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.ReportType;

import java.util.UUID;

public record ReportCommand(
         ReportType reportType,

         String email,

         ReportScope reportScope,

         UUID scopeEntityId
) {
}
