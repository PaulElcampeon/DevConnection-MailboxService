package com.devconnection.MailboxService.messages;

import com.devconnection.MailboxService.domain.Mail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMailMessage {
    private String email;
    private String mailMessageOwner;
    private Mail mail;
}
