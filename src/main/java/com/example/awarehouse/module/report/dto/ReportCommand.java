package com.example.awarehouse.module.report.dto;

import com.example.awarehouse.module.report.ReportInterval;
import com.example.awarehouse.module.report.ReportScope;

import java.util.UUID;

public record ReportCommand(
         ReportInterval reportInterval,

         String email,

         ReportScope reportScope,

         UUID scopeEntityId
) {
}
