package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.report.dto.ReportCommand;

import java.util.List;

public interface ReportCommandCreator {
     void sendReportCommand(List<ReportCommand> command);
}
