package com.example.social_media.models.user;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 16, message = "Username must be 4-16 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only Latin letters or numbers")
    private String username;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 32, message = "First name must be 2-32 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only Latin letters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 64, message = "Last name must be 2-64 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only Latin letters")
    private String lastName;

    @NotNull(message = "Date of birth cannot be null")
    @PastOrPresent(message = "Date of birth must not be in the future")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
    private String password;

    @NotBlank(message = "Role cannot be empty")
    private String role;
}
