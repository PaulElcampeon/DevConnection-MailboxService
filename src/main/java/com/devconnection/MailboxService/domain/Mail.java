package com.devconnection.MailboxService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    private String owner;
    private List<MailContent> messages = new ArrayList<>();

    public Mail(String owner) {
        this.owner = owner;
    }
}
