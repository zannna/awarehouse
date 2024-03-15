package com.example.awarehouse.module.report;

import com.example.awarehouse.module.report.command.ReportCommandCreator;
import com.example.awarehouse.module.report.dto.ReportCommand;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReportScheduler {
private ReportRepository reportRepository;
private ReportCommandCreator reportCommandCreator;
private static final int PAGINATION_SIZE = 100;

   // @Scheduled(cron = "0 * * * * *")
    public void generateReport() {
       ZonedDateTime UTCNow = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
       handleReportPage(UTCNow, PageRequest.of(0, PAGINATION_SIZE));
    }

    private void handleReportPage(ZonedDateTime UTCNow, Pageable pageable) {
        Page<Report> page = getPage(UTCNow, pageable);
        List<Report> reports = page.getContent();
        List<ReportCommand> reportCommands = buildCommand(reports);
        adjustNextGenerationTime(reports);
        reportCommandCreator.sendReportCommand(reportCommands);

        if (page.hasNext()) {
            handleReportPage(UTCNow, page.nextPageable());
        }
    }

    private void adjustNextGenerationTime(List<Report> reports) {
        reports.forEach((report)->
        {
            report.adjustNextGenerationTime();
            reportRepository.save(report);
        });
    }

    private List<ReportCommand> buildCommand(List<Report> reports) {
        List<ReportCommand> reportCommands = reports.stream().map(ReportMapper::toCommand).collect(Collectors.toList());
        return reportCommands;
    }

    private Page<Report> getPage(ZonedDateTime UTCNow, Pageable pageable) {
        return  reportRepository.findToGenerate(UTCNow,
                PageRequest.of(0, PAGINATION_SIZE));
    }

}
