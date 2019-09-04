package com.devconnection.MailboxService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.devconnection.MailboxService.messages.DeleteMailMessage;
import com.devconnection.MailboxService.messages.GenericMessage;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createPostbox(@RequestBody GenericMessage genericMessage) {
        postboxService.createPostbox(genericMessage);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public GetPostboxResponse getPostbox(@RequestBody GetPostboxMessage getPostboxMessage) {
        return postboxService.getPostbox(getPostboxMessage);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void sendMailMessage(@RequestBody  SendMailMessage sendMailMessage) {
        postboxService.sendMailMessage(sendMailMessage);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public GenericResponse deleteMail(@RequestBody  DeleteMailMessage deleteMailMessage) {
        return new GenericResponse(postboxService.deleteMail(deleteMailMessage));
    }
}
