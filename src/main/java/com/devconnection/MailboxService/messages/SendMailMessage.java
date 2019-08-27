package com.devconnection.MailboxService.messages;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailMessage {

    private String email;
    private String sender;
    private String content;
    private long timeSent;
}
