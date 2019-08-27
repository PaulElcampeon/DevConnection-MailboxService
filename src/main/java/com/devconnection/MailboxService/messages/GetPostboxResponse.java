package com.devconnection.MailboxService.messages;

import com.devconnection.MailboxService.domain.Postbox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostboxResponse {

    private Postbox postbox;

}
