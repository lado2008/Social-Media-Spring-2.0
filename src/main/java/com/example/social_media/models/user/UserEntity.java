
package com.example.social_media.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "user_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 16, message = "Username must be 4-16 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only Latin letters or numbers")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 32, message = "First name must be 2-32 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only Latin letters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 64, message = "Last name must be 2-64 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only Latin letters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull(message = "Date of birth cannot be null")
    @PastOrPresent(message = "Date of birth must not be in the future")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
}
