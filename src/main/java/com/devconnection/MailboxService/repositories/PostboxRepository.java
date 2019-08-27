package com.devconnection.MailboxService.repositories;

import com.devconnection.MailboxService.domain.Postbox;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostboxRepository  extends MongoRepository<Postbox, String> {
}
