package com.geronimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geronimo.GeronimoSpringTest;
import com.geronimo.TestUtil;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class MessageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessageService messageService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void before() {
        mockMvc = TestUtil.mockMvcWithSecurity(context);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    public void testGetMessage() throws Exception {
        User author = new User("john", "doe");
        author.setId(1L);

        Message msg = new Message("oh my god, look at her butt", author);
        msg.setId(1L);

        when(messageService.getMessageById(msg.getId())).thenReturn(msg);

        mockMvc.perform(get("/user/1/message/1").with(user("user"))
                .header("Authorization", "Bearer fakeToken"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"version\":null,\"dateCreated\":null,\"lastUpdated\":null,\"author\":{\"id\":1,\"version\":null,\"dateCreated\":null,\"lastUpdated\":null,\"username\":\"john\",\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true,\"lastPasswordReset\":null,\"profile\":null},\"text\":\"oh my god, look at her butt\",\"likes\":[],\"reblogs\":[],\"answers\":[]}"));

        verify(messageService, times(1)).getMessageById(msg.getId());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    public void testPostMessage() throws Exception {
        User author = new User("john", "doe");
        author.setId(1L);

        Message msg = new Message("Hola, amigos", author);

        mockMvc.perform(post("/user/1/message").with(user("user"))
                .header("Authorization", "Bearer fakeToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msg)))
                .andExpect(status().isCreated());

        // TODO: how to apply Mockito matcher here? eq(), same(), e.g. `...postMessage(eq(msg))`?
        verify(messageService, times(1)).postMessage(any(Message.class));
    }
}
