package com.example.awarehouse.module.report;

import com.example.awarehouse.module.report.dto.ReportCommand;
import com.example.awarehouse.module.report.dto.ReportDto;

public class ReportMapper {
    public  static ReportDto toDto(Report report, String entityName){
        return new ReportDto(report.getId(), report.getReportInterval(), report.getEmail(), entityName);
    }

    public static ReportCommand toCommand(Report report){
        return new ReportCommand(report.getReportInterval(), report.getEmail(), report.getReportScope(), report.getScopeEntityId());
    }
}

