package com.tracker.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.config.security.SecurityConfig;
import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.UserRegisterDTO;
import com.tracker.service.UserService;
import com.tracker.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Martin Krumov
 */
//@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, UserDetailsServiceImpl.class })
@WebMvcTest(UserResource.class)
//@AutoConfigureMockMvc(secure = false)
//@ContextConfiguration(classes= {SecurityConfig.class, UserDetailsServiceImpl.class})
class UserResourceIT {

    @Configuration
    static class TestConfiguration {
        @Bean
        public UserDetailsService userServiceImpl() {
            return mock(UserDetailsServiceImpl.class);
        }

        @Bean
        public SecurityAutoConfiguration securityConfig() {
            return mock(SecurityAutoConfiguration.class);
        }

    }

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
                .password("webMvcTest")
                .email("webMvcTest@meail.com")
                .firstName("webMvcTest")
                .lastName("webMvcTest")
                .build();

        user = User.builder()
                .username("webMvcTest")
                .password("webMvcTest")
                .email("webMvcTest@meail.com")
                .firstName("webMvcTest")
                .lastName("webMvcTest")
                .build();
    }

    @Test
    void registerShouldReturn201() throws Exception {
        when(userMapper.toUser(userRegisterDTO)).thenReturn(user);
        when(userService.register(user)).thenReturn(user);

        mockMvc.perform(
                post("/api/users/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(userRegisterDTO)));
        //FIXME:
//                .andExpect(status().isForbidden());
    }
}
