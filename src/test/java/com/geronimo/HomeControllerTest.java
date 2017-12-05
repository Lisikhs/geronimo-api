package com.geronimo;

import com.geronimo.controller.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@ActiveProfiles("test")
@WithMockUser(username = "root", roles = { "ADMIN_HOME" })
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin").with(user("root")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Restricted only for admins")));
    }

    @Test
    public void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/").with(user("root")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello world")));
    }

}
