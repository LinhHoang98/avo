package com.linhhn.zync.user.controller;

import com.linhhn.zync.user.MongoDbTestBase;
import com.linhhn.zync.user.dto.RegistrationDto;
import com.linhhn.zync.user.dto.UserPatchDto;
import com.linhhn.zync.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends MongoDbTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll().block();
    }

    @Test
    void testRegister_OK() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");

        executeRegister(registration).expectStatus().isOk();
    }

    @Test void testRegister_NOK_PasswordNotMatch() {
        RegistrationDto registration = new RegistrationDto("username", "notmatchpassword", "password", "fullname");

        executeRegister(registration).expectStatus().isBadRequest();
    }

    @Test void testRegister_NOK_UsernameAlreadyExists() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");

        executeRegister(registration);
        executeRegister(registration)
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test void testGetUserByUsername_OK() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");
        executeRegister(registration);

        executeGetUserByUsername("username").expectStatus().isOk();
    }

    @Test void testGetUserByUsername_NOK_NotFoundUser() {
        executeGetUserByUsername("username").expectStatus().isNotFound();
    }

    @Test void testUpdateUser_OK_UpdatePassword() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");
        executeRegister(registration);

        UserPatchDto userPatchDto = new UserPatchDto("new-password", null, null);
        executeUpdateUser("username", userPatchDto).expectStatus().isOk();
    }

    @Test void testUpdateUser_OK_UpdateFullname() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");
        executeRegister(registration);

        UserPatchDto userPatchDto = new UserPatchDto(null, "new-fullname", null);
        executeUpdateUser("username", userPatchDto).expectStatus().isOk();
    }

    @Test void testUpdateUser_OK_UpdateImageUrl() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");
        executeRegister(registration);

        UserPatchDto userPatchDto = new UserPatchDto(null, null, "imageUrl");
        executeUpdateUser("username", userPatchDto).expectStatus().isOk();
    }

    @Test void testIsUserExist_OK_FoundUser() {
        RegistrationDto registration = new RegistrationDto("username", "password", "password", "fullname");
        executeRegister(registration);

        executeIsUserExist("username")
                .expectStatus().isOk()
                .expectBody(Boolean.class).value(response -> assertEquals(true, response));
    }

    @Test void testIsUserExist_OK_NotFoundUser() {
        executeIsUserExist("username")
                .expectStatus().isOk()
                .expectBody(Boolean.class).value(response -> assertEquals(false, response));
    }

    private WebTestClient.ResponseSpec executeRegister(RegistrationDto registration) {
        return client.post()
                .uri("/users/registration")
                .bodyValue(registration)
                .exchange();
    }

    private WebTestClient.ResponseSpec executeGetUserByUsername(String username) {
        return client.get().uri("/users/" + username).exchange();
    }

    private WebTestClient.ResponseSpec executeUpdateUser(String username, UserPatchDto userPatchDto) {
        return client.patch().uri("/users/" + username).bodyValue(userPatchDto).exchange();
    }

    private WebTestClient.ResponseSpec executeIsUserExist(String username) {
        return client.get().uri("/users/" + username + "/exist").exchange();
    }
}
