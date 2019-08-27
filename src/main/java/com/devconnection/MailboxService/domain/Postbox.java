package com.devconnection.MailboxService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "POSTBOXS")
public class Postbox {

    @Id
    private String email;
    private List<Mail> mailList = new ArrayList<>();

    public Postbox(String email) {
        this.email = email;
    }
}
