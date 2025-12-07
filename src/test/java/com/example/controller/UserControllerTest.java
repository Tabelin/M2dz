package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        UserDto inputDto = new UserDto("Test User", "test@example.com", 30);
        UserDto outputDto = new UserDto();
        outputDto.setId(1L);
        outputDto.setName("Test User");
        outputDto.setEmail("test@example.com");
        outputDto.setAge(30);


        when(userService.createUser(any())).thenReturn(outputDto);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("{\"name\":\"Test User\",\"email\":\"test@example.com\",\"age\":30}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setAge(30);

        when(userService.findById(1L)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDto inputDto = new UserDto("Updated Name", "updated@example.com", 35);
        UserDto outputDto = new UserDto();
        outputDto.setId(1L);
        outputDto.setName("Updated Name");
        outputDto.setEmail("updated@example.com");
        outputDto.setAge(35);


        when(userService.updateUser(eq(1L), any())).thenReturn(outputDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\",\"age\":35}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setAge(30);

        when(userService.findAll()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }
}