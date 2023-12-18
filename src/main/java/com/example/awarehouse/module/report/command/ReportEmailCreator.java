package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.email.dto.EmailDto;
import com.example.awarehouse.module.report.dto.ReportCommand;

public interface ReportEmailCreator {
    EmailDto createEmail(ReportCommand command);
}
