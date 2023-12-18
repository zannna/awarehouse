package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.report.dto.ReportCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BasicReportCommandSender implements ReportCommandCreator {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.report.key}")
    private String reportKey;

    @Override
    public void sendReportCommand(List<ReportCommand> command) {
        rabbitTemplate.convertAndSend(exchange, reportKey, command);
    }
}
