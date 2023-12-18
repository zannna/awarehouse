package com.example.awarehouse.module.report;

import com.example.awarehouse.module.report.dto.ReportCreationDto;
import com.example.awarehouse.module.report.dto.ReportDto;
import com.example.awarehouse.module.token.BasicTokenGenerator;
import com.example.awarehouse.util.TimeSupplier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class ReportService {
    BasicTokenGenerator basicTokenGenerator;
    TimeSupplier timeSupplier;
    ReportRepository reportRepository;

    public ReportDto setUnderstockReport(ReportCreationDto reportCreationDto) {
        ZonedDateTime time = timeSupplier.geTime();
        Report report = new Report(time, reportCreationDto.reportType(), reportCreationDto.email(), reportCreationDto.reportScope(), reportCreationDto.id());
        report= reportRepository.save(report);
        setToken(report);
        return ReportMapper.toDto(report);
    }
    private void setToken(Report report){
        String salt = basicTokenGenerator.generateSalt();
        String token= basicTokenGenerator.generateToken(report.getId().toString(), salt);
    }
}
