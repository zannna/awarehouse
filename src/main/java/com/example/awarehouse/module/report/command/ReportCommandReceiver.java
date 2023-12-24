package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.report.dto.ReportCommand;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ReportCommandReceiver {
    ReportCommandHandler reportCommandHandler;
    @RabbitListener(queues = "${rabbitmq.queue.report.name}")
    public void consumeReportCommand(List<ReportCommand> reportCommands) {
       reportCommandHandler.handle(reportCommands);
    }

}
