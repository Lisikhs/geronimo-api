package com.geronimo.controller;

import com.geronimo.GeronimoSpringTest;
import com.geronimo.TestUtil;
import com.geronimo.config.security.UserDetails;
import com.geronimo.config.security.UserDetailsFactory;
import com.geronimo.config.security.jwt.JwtTokenUtil;
import com.geronimo.model.User;
import com.geronimo.service.UserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class HomeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtTokenUtil tokenUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        mockMvc = TestUtil.mockMvcWithSecurity(context);
    }

    @Test
    public void testGetSecuredEndpointWithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testEndpointWithAdminRoleRequiredSucceedsForUserWithAdminRole() throws Exception {
        User user = new User();
        user.setUsername("dummy");
        user.setEnabled(Boolean.TRUE);

        UserDetails jwtUser = UserDetailsFactory.fromUser(user);

        when(tokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

        mockMvc.perform(get("/admin").header("Authorization", "Bearer dummy"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Only for authorized admins")));
    }

    @Test
    public void testSecuredEndpointSucceedsForAuthorizedUser() throws Exception {
        mockMvc.perform(get("/user").with(user("root")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Only for authorized users")));
    }

    @Test
    public void testSecuredEndpointFailsWithPublicAccess() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUnsecuredEndpointSucceedsWithUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Public for all")));
    }

}
