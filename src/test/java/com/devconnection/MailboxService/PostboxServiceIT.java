package com.devconnection.MailboxService;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.devconnection.MailboxService.domain.Mail;
import com.devconnection.MailboxService.domain.MailContent;
import com.devconnection.MailboxService.domain.Postbox;
import com.devconnection.MailboxService.messages.DeleteMailMessage;
import com.devconnection.MailboxService.messages.GetPostboxMessage;
import com.devconnection.MailboxService.messages.GetPostboxResponse;
import com.devconnection.MailboxService.messages.SendMailMessage;
import com.devconnection.MailboxService.repositories.PostboxRepository;
import com.devconnection.MailboxService.services.PostboxService;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@SpringBootTest(classes = {PostboxServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PostboxServiceIT {

    @Autowired
    private PostboxService postboxService;

    @Autowired
    private PostboxRepository postboxRepository;

    private final String postboxOwner = "dave@live.co.uk";

    @Before
    public void init() {
        Postbox postbox = new Postbox(postboxOwner);
        postboxRepository.insert(postbox);
    }

    @After
    public void tearDown() {
        postboxRepository.deleteAll();
    }

    @Test
    public void getPostbox() {
        GetPostboxMessage getPostboxMessage = new GetPostboxMessage(postboxOwner);
        GetPostboxResponse getPostboxResponse = postboxService.getPostbox(getPostboxMessage);

        assertEquals(postboxOwner, getPostboxResponse.getPostbox().getEmail());
    }

    @Test
    public void sendMailMessage_addsNewMail() {
        SendMailMessage message = new SendMailMessage();
        message.setContent("Hello");
        message.setEmail(postboxOwner);
        message.setSender("james@live.co.uk");

        postboxService.sendMailMessage(message);

        Postbox retrievedPostbox = postboxRepository.findById(postboxOwner).get();
        Mail mail = retrievedPostbox.getMailList().get(0);
        MailContent mailContent = retrievedPostbox.getMailList().get(0).getMessages().get(0);

        assertEquals(1, retrievedPostbox.getMailList().size());
        assertEquals(1, mail.getMessages().size());
        assertEquals(message.getSender(), mailContent.getSender());
        assertEquals(message.getContent(), mailContent.getContent());
    }

    @Test
    public void sendMailMessage_addsToExistingMail() {
        SendMailMessage produceNewMail = new SendMailMessage();
        produceNewMail.setContent("Hello");
        produceNewMail.setEmail(postboxOwner);
        produceNewMail.setSender("james@live.co.uk");

        SendMailMessage addToExistingMail = new SendMailMessage();
        addToExistingMail.setContent("Bye bye");
        addToExistingMail.setEmail(postboxOwner);
        addToExistingMail.setSender("james@live.co.uk");

        postboxService.sendMailMessage(produceNewMail);
        postboxService.sendMailMessage(addToExistingMail);

        Postbox retrievedPostbox = postboxRepository.findById(postboxOwner).get();
        Mail mail = retrievedPostbox.getMailList().get(0);
        MailContent mailContent = retrievedPostbox.getMailList().get(0).getMessages().get(1);

        assertEquals(1, retrievedPostbox.getMailList().size());
        assertEquals(2, mail.getMessages().size());
        assertEquals(addToExistingMail.getSender(), mailContent.getSender());
        assertEquals(addToExistingMail.getContent(), mailContent.getContent());
    }

    @Test
    public void sendMailMessage_ShouldBe2Mails() {
        SendMailMessage produceNewMail1 = new SendMailMessage();
        produceNewMail1.setContent("Hello");
        produceNewMail1.setEmail(postboxOwner);
        produceNewMail1.setSender("james@live.co.uk");

        SendMailMessage produceNewMail2 = new SendMailMessage();
        produceNewMail2.setContent("Bye bye");
        produceNewMail2.setEmail(postboxOwner);
        produceNewMail2.setSender("david@live.co.uk");

        postboxService.sendMailMessage(produceNewMail1);
        postboxService.sendMailMessage(produceNewMail2);

        Postbox retrievedPostbox = postboxRepository.findById(postboxOwner).get();

        assertEquals(2, retrievedPostbox.getMailList().size());
    }

    @Test
    public void deleteMail() {
        SendMailMessage produceNewMail1 = new SendMailMessage();
        produceNewMail1.setContent("Hello");
        produceNewMail1.setEmail(postboxOwner);
        produceNewMail1.setSender("james@live.co.uk");

        SendMailMessage produceNewMail2 = new SendMailMessage();
        produceNewMail2.setContent("Bye bye");
        produceNewMail2.setEmail(postboxOwner);
        produceNewMail2.setSender("david@live.co.uk");

        postboxService.sendMailMessage(produceNewMail1);
        postboxService.sendMailMessage(produceNewMail2);

        DeleteMailMessage deleteMailMessage = new DeleteMailMessage();
        deleteMailMessage.setEmail(postboxOwner);
        deleteMailMessage.setMailMessageOwner("james@live.co.uk");
        deleteMailMessage.setMail(postboxRepository.findById(postboxOwner).get().getMailList().get(0));

        postboxService.deleteMail(deleteMailMessage);

        Postbox postbox = postboxRepository.findById(postboxOwner).get();

        assertEquals(1, postbox.getMailList().size());
    }
}
