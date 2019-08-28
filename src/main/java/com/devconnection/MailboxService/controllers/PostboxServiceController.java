package com.devconnection.MailboxService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.devconnection.MailboxService.messages.DeleteMailMessage;
import com.devconnection.MailboxService.messages.GenericResponse;
import com.devconnection.MailboxService.messages.GetPostboxMessage;
import com.devconnection.MailboxService.messages.GetPostboxResponse;
import com.devconnection.MailboxService.messages.SendMailMessage;
import com.devconnection.MailboxService.services.PostboxService;

@RestController
public class PostboxServiceController {

    private PostboxService postboxService;

    @Autowired
    public PostboxServiceController(PostboxService postboxService) {
        this.postboxService = postboxService;
    }

    @RequestMapping(value = "/postbox-service/get", method = RequestMethod.POST)
    GetPostboxResponse getPostbox(@RequestBody GetPostboxMessage getPostboxMessage) {
        return postboxService.getPostbox(getPostboxMessage);
    }

    @RequestMapping(value = "/postbox-service/send", method = RequestMethod.POST)
    public void sendMailMessage(@RequestBody  SendMailMessage sendMailMessage) {
        postboxService.sendMailMessage(sendMailMessage);
    }

    @RequestMapping(value = "/postbox-service/delete", method = RequestMethod.POST)
    GenericResponse deleteMail(@RequestBody  DeleteMailMessage deleteMailMessage) {
        return new GenericResponse(postboxService.deleteMail(deleteMailMessage));
    }
}
