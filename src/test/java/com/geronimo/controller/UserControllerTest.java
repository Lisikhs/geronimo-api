package com.geronimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geronimo.GeronimoSpringTest;
import com.geronimo.TestUtil;
import com.geronimo.model.User;
import com.geronimo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void before() {
        mockMvc = TestUtil.mockMvcWithSecurity(context);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    public void getCurrent() throws Exception {

        User user = new User("john", "doe");
        user.setId(1L);

        when(userService.getCurrent()).thenReturn(user);

        mockMvc.perform(get("/user/current").with(user("user"))
                .header("Authorization", "Bearer fakeToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

        verify(userService, times(1)).getCurrent();
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    public void getById() throws Exception {

        User user = new User("john", "doe");
        user.setId(1L);

        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(get("/user/1").with(user("user"))
                .header("Authorization", "Bearer fakeToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

        verify(userService, times(1)).getById(user.getId());
    }
}
