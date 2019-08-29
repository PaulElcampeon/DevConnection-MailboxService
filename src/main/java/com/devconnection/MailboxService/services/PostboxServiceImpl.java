package com.devconnection.MailboxService.services;

import com.devconnection.MailboxService.domain.Mail;
import com.devconnection.MailboxService.domain.MailContent;
import com.devconnection.MailboxService.domain.Postbox;
import com.devconnection.MailboxService.messages.*;
import com.devconnection.MailboxService.repositories.PostboxRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostboxServiceImpl implements PostboxService {

    private MongoTemplate mongoTemplate;

    private PostboxRepository postboxRepository;

    @Autowired
    public PostboxServiceImpl(PostboxRepository postboxRepository, MongoTemplate mongoTemplate) {
        this.postboxRepository = postboxRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public GetPostboxResponse getPostbox(GetPostboxMessage getPostboxMessage) {
        return new GetPostboxResponse(postboxRepository.findById(getPostboxMessage.getEmail()).get());
    }

    @Override
    public void sendMailMessage(SendMailMessage sendMailMessage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(sendMailMessage.getEmail())
                .and("mailList.owner").is(sendMailMessage.getSender()));

        List<Postbox> retrievedPostboxs = mongoTemplate.find(query, Postbox.class);

        Query newQuery = new Query();
        Update update = new Update();
        MailContent mailContent =
                new MailContent(
                        UUID.randomUUID().toString(),
                        sendMailMessage.getSender(),
                        sendMailMessage.getContent(),
                        System.currentTimeMillis()
                );

        if (retrievedPostboxs.size() == 0) {
            Mail mail = new Mail(sendMailMessage.getSender());
            mail.getMessages().add(mailContent);

            newQuery.addCriteria(Criteria.where("_id").is(sendMailMessage.getEmail()));

            update.addToSet("mailList", mail);

        } else {

            newQuery.addCriteria(Criteria.where("_id").is(sendMailMessage.getEmail())
                    .and("mailList.owner").is(sendMailMessage.getSender()));

            update.addToSet("mailList.$.messages", mailContent);
        }

        mongoTemplate.updateFirst(newQuery, update, Postbox.class);
    }

    @Override
    public boolean deleteMail(DeleteMailMessage deleteMailMessage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(deleteMailMessage.getEmail())
        .and("mailList.owner").is(deleteMailMessage.getMailMessageOwner()));

        Update update = new Update();
        update.pull("mailList", deleteMailMessage.getMail());

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Postbox.class);

        return updateResult.getModifiedCount() == 1;
    }

    @Override
    public void createPostbox(GenericMessage genericMessage) {
        postboxRepository.insert(new Postbox(genericMessage.getEmail()));
    }
}
