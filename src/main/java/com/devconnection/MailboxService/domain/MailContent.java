package com.devconnection.MailboxService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailContent {
    private String id;
    private String sender;
    private String content;
    private long timeSent;
}
