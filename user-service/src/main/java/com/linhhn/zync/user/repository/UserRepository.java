package com.linhhn.zync.user.repository;

import com.linhhn.zync.user.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findUserByUsername(String username);
}
