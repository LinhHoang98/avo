package com.linhhn.zync.user.service;

import com.linhhn.zync.user.dto.RegistrationDto;
import com.linhhn.zync.user.dto.UserPatchDto;
import com.linhhn.zync.user.entity.User;
import com.linhhn.zync.user.repository.UserRepository;
import com.linhhn.zync.util.exception.BadRequestException;
import com.linhhn.zync.util.exception.ResourceAlreadyExistsException;
import com.linhhn.zync.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> register(RegistrationDto registration) {
        if (!registration.password().trim().equals(registration.confirmPassword().trim())) {
            return Mono.error(new BadRequestException("Passwords do not match"));
        }

        return isUserExist(registration.username())
                .flatMap(isExist -> {
                    if (isExist) {
                        return Mono.error(new ResourceAlreadyExistsException(registration.username()));
                    } else {
                        User user = new User();
                        user.setUsername(registration.username().trim());
                        user.setPassword(registration.password().trim());
                        user.setFullname(registration.fullname().trim());
                        return userRepository.save(user);
                    }
                });
    }

    public Mono<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Username " + username + " not found")));
    }

    public Mono<User> updateUser(String username, UserPatchDto user) {
        return getUserByUsername(username)
                .flatMap(existingUser -> {
                    if (user.password() != null) {
                        existingUser.setPassword(user.password());
                    }

                    if (user.fullname() != null) {
                        existingUser.setFullname(user.fullname());
                    }

                    if (user.imageUrl() != null) {
                        existingUser.setImageUrl(user.imageUrl());
                    }
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Boolean> isUserExist(String username) {
        return userRepository.findUserByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false);
    }
}