package com.example.awarehouse.module.email;

import com.example.awarehouse.module.email.dto.EmailDto;

public interface EmailSender {
    void sendEmail(EmailDto email);
}
