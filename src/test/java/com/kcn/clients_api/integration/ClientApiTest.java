package com.kcn.clients_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcn.clients_api.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // USER -> can only read
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userShouldGetClients() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Robert"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userShouldNotCreateClients() throws Exception {
        ClientDTO dto = new ClientDTO("Test", "User", "test.user@mail.com");
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userShouldNotUpdateClients() throws Exception {
        ClientDTO dto = new ClientDTO("Updated", "Name", "updated@mail.com");
        mockMvc.perform(put("/api/clients/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userShouldNotDeleteClients() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isForbidden());
    }

    // MANAGER -> can add and edit, but cannot delete
    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void managerShouldCreateClient() throws Exception {
        ClientDTO dto = new ClientDTO("Test", "User", "test.user@mail.com");
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void managerShouldUpdateClient() throws Exception {
        ClientDTO dto = new ClientDTO("Updated", "Name", "updated@mail.com");
        mockMvc.perform(put("/api/clients/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void managerShouldNotDeleteClients() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isForbidden());
    }

    // ADMIN -> can delete
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminShouldDeleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }
}
