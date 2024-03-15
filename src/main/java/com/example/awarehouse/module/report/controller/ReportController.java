package com.example.awarehouse.module.report.controller;

import com.example.awarehouse.module.report.ReportService;
import com.example.awarehouse.module.report.command.BasicReportCommandSender;
import com.example.awarehouse.module.report.dto.ReportCreationDto;
import com.example.awarehouse.module.report.dto.ReportDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1 + URI_REPORT)
@AllArgsConstructor
public class ReportController {

    ReportService reportService;
    BasicReportCommandSender basicReportCommandSender;

    @PostMapping(URI_UNDERSTOCK)
    public ReportDto setReportForUnderstockedProductsInWarehouses(@Valid @RequestBody ReportCreationDto reportCreationDto) {
        return reportService.setUnderstockReport(reportCreationDto);
    }

    @GetMapping
    public List<ReportDto> getUserReports() {
        return reportService.getUserReports();
    }
}
