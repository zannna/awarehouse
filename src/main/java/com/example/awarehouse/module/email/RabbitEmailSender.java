package com.example.awarehouse.module.email;

import com.example.awarehouse.module.email.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitEmailSender implements EmailSender{

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.mail.key}")
    private String mailKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailDto email) {
        rabbitTemplate.convertAndSend(exchange, mailKey, email);
    }

}
