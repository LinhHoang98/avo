package com.linhhn.zync.user.controller;

import com.linhhn.zync.user.dto.RegistrationDto;
import com.linhhn.zync.user.dto.UserPatchDto;
import com.linhhn.zync.user.entity.User;
import com.linhhn.zync.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public Mono<User> register(@RequestBody RegistrationDto registration) {
        return userService.register(registration);
    }

    @GetMapping("/{username}")
    public Mono<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/{username}")
    public Mono<User> updateUser(@PathVariable String username, @RequestBody UserPatchDto user) {
        return userService.updateUser(username, user);
    }

    @GetMapping("/{username}/exist")
    public Mono<Boolean> isUserExist(@PathVariable String username) {
        return userService.isUserExist(username);
    }
}
