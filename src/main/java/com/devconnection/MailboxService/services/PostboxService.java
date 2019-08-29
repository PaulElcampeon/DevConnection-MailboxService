package com.devconnection.MailboxService.services;

import com.devconnection.MailboxService.messages.*;

public interface PostboxService {

    GetPostboxResponse getPostbox(GetPostboxMessage getPostboxMessage);

    void sendMailMessage(SendMailMessage sendMailMessage);

    boolean deleteMail(DeleteMailMessage deleteMailMessage);

    void createPostbox(GenericMessage genericMessage);
}
