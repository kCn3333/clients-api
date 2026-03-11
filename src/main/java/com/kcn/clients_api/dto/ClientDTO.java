package com.kcn.clients_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters ")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters ")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    public ClientDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
