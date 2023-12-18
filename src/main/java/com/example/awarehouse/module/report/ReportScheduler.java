package com.example.awarehouse.module.report;

import com.example.awarehouse.module.report.command.ReportCommandCreator;
import com.example.awarehouse.module.report.dto.ReportCommand;
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
public class ReportScheduler {
private ReportRepository reportRepository;
private ReportCommandCreator reportCommandCreator;
private static final int PAGINATION_SIZE = 100;

    @Scheduled(cron = "0 0 * * * ?")
    public void generateReport() {
       ZonedDateTime UTCNow = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);


    }

    private void handleReportPage(ZonedDateTime UTCNow, Pageable pageable) {
        Page<Report> page = getPage(UTCNow, pageable);
        List<ReportCommand> reports = buildCommand(page);
        reportCommandCreator.sendReportCommand(reports);

        if (page.hasNext()) {
            handleReportPage(UTCNow, page.nextPageable());
        }
    }

    private List<ReportCommand> buildCommand(Page<Report> page) {
        List<ReportCommand> reports = page.getContent().stream().map(ReportMapper::toCommand).collect(Collectors.toList());
        return reports;
    }

    private Page<Report> getPage(ZonedDateTime UTCNow, Pageable pageable) {
        return  reportRepository.findToGenerate(UTCNow,
                PageRequest.of(0, PAGINATION_SIZE));
    }

}
