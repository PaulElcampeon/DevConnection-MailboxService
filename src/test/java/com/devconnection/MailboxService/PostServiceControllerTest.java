package com.devconnection.MailboxService;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devconnection.MailboxService.domain.Mail;
import com.devconnection.MailboxService.domain.Postbox;
import com.devconnection.MailboxService.messages.DeleteMailMessage;
import com.devconnection.MailboxService.messages.GetPostboxMessage;
import com.devconnection.MailboxService.messages.GetPostboxResponse;
import com.devconnection.MailboxService.messages.SendMailMessage;
import com.devconnection.MailboxService.repositories.PostboxRepository;
import com.devconnection.MailboxService.services.PostboxService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostboxServiceApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostServiceControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private PostboxService postboxService;

    @Autowired
    private PostboxRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "http://localhost:";

    private String postboxOwner;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        postboxOwner = "James@live.co.uk";
        Postbox postbox = new Postbox(postboxOwner);
        repository.insert(postbox);
        baseUrl += port + "/";
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }


    @Test
    public void getPostbox() {
        GetPostboxMessage getPostboxMessage = new GetPostboxMessage(postboxOwner);

        ResponseEntity<GetPostboxResponse> responseEntity = restTemplate.postForEntity(baseUrl + "postbox-service/get", getPostboxMessage, GetPostboxResponse.class);

        assertEquals(postboxOwner, responseEntity.getBody().getPostbox().getEmail());
    }

    @Test
    public void sendMailMessage() {
        String sender = "dave@live.co.uk";
        SendMailMessage sendMailMessage = new SendMailMessage();
        sendMailMessage.setContent("Hello");
        sendMailMessage.setEmail(postboxOwner);
        sendMailMessage.setSender(sender);

        restTemplate.postForEntity(baseUrl + "postbox-service/send", sendMailMessage, String.class);

        assertEquals(sender, repository.findById(postboxOwner).get().getMailList().get(0).getOwner());
    }

    @Test
    public void deleteMail() {
        String sender = "dave@live.co.uk";

        SendMailMessage sendMailMessage = new SendMailMessage();
        sendMailMessage.setContent("Hello");
        sendMailMessage.setEmail(postboxOwner);
        sendMailMessage.setSender(sender);

        postboxService.sendMailMessage(sendMailMessage);

        Mail mail = repository.findById(postboxOwner).get().getMailList().get(0);

        DeleteMailMessage deleteMailMessage = new DeleteMailMessage();
        deleteMailMessage.setMail(mail);
        deleteMailMessage.setEmail(postboxOwner);
        deleteMailMessage.setMailMessageOwner(sender);

        restTemplate.postForEntity(baseUrl + "postbox-service/delete", deleteMailMessage, String.class);

        assertEquals(0, repository.findById(postboxOwner).get().getMailList().size());

    }
}
