package com.tracker.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.config.security.SecurityConfig;
import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.ResetPasswordDTO;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import com.tracker.service.UserService;
import com.tracker.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Martin Krumov
 */
@Disabled//TODO: fix response 404 on tests
@Import(SecurityConfig.class)
@WebMvcTest(UserResource.class)
class UserResourceIT {

    @Configuration
    public static class TestConfiguration {

        @Bean
        public UserDetailsService userDetailsServiceImpl() {
            return mock(UserDetailsServiceImpl.class);
        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

    }

    private static final String REGISTER_URL = "/api/users/register";
    private static final String COMPLETE_REGISTER_URL = "/api/users/complete-register";
    private static final String RESET_PASSWORD_URL = "/api/users/reset-password";
    private static final String VALIDATE_TOKEN_URL = "/api/users/validate-token";
    private static final String CHANGE_PASSWORD_URL = "/api/users/change-password";
    private static final String USERS_PAGE_URL = "/api/users";

    private static final String TOKEN = "token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    void setUp() {
        userRegisterDTO = UserRegisterDTO.builder()
                .username("webMvcTest")
                .password("webMvcTest1!")
                .email("webMvcTest@meail.com")
                .firstName("webMvcTest")
                .lastName("webMvcTest")
                .build();

        user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(userRegisterDTO.getPassword())
                .email(userRegisterDTO.getEmail())
                .firstName(userRegisterDTO.getFirstName())
                .lastName(userRegisterDTO.getLastName())
                .build();
    }

    @WithMockUser(value = "spring")
    @Test
    void registerShouldReturn201() throws Exception {
        //arrange
        when(userMapper.toUser(userRegisterDTO)).thenReturn(user);
        when(userMapper.toDtoIgnorePassword(user)).thenReturn(UserRegisterDTO.builder().email(user.getEmail()).build());
        when(userService.register(user)).thenReturn(user);

        //act-assert
        mockMvc.perform(
                post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.password", nullValue()));
    }

    @Test
    @WithMockUser(value = "spring")
    void completeRegister_WithoutToken_Returns400() throws Exception {
        mockMvc.perform(get(COMPLETE_REGISTER_URL)
                        .param(TOKEN, "")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void completeRegister_WithToken_Returns200() throws Exception {
        String verificationToken = UUID.randomUUID().toString();
        doNothing().when(userService).completeRegistration(verificationToken);

        mockMvc.perform(get(COMPLETE_REGISTER_URL)
                        .param(TOKEN, verificationToken))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_WithEmail_Returns200() throws Exception {
        doNothing().when(userService).resetPassword(user.getEmail());

        mockMvc.perform(post(RESET_PASSWORD_URL)
                .param("email", user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_WithInvalidEmail_Returns400() throws Exception {
        doThrow(new IllegalArgumentException("Not valid")).when(userService).resetPassword(any());

        mockMvc.perform(post(RESET_PASSWORD_URL)
                        .param("email", "aaa@aa.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validateToken_WithToken_Returns200() throws Exception {
        String token = UUID.randomUUID().toString();
        doNothing().when(userService).validateToken(token);

        mockMvc.perform(get(VALIDATE_TOKEN_URL)
                        .param(TOKEN, token))
                .andExpect(status().isOk());
    }

    @Test
    void changePassword_Returns200() throws Exception {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder()
                .password("Password123!")
                .token(UUID.randomUUID().toString())
                .build();

        doNothing()
                .when(userService).completePasswordReset(resetPasswordDTO.getToken(), resetPasswordDTO.getPassword());

        mockMvc.perform(
                post(CHANGE_PASSWORD_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(resetPasswordDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void findUsersShouldReturn200() throws Exception {
        //arrange
        List<User> users = List.of(user);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 20), users.size());
        UserInfoDTO userInfoDTO = new UserInfoDTO("test", "mail@mail.com", "test", "test");

        when(userService.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.usersToUserInfoDTOs(userPage.getContent())).thenReturn(List.of(userInfoDTO));

        //act-assert
        mockMvc.perform(get(USERS_PAGE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }
}
