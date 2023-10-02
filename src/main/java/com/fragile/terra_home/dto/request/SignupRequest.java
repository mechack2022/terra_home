package com.fragile.terra_home.dto.request;

import com.fragile.terra_home.constants.UserRole;
import com.fragile.terra_home.utils.validations.PhoneNumber;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message ="lastName is required")
    private String lastName;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;


    @NotBlank(message = "mobile number is required")
    @PhoneNumber
    private String mobileNumber;
}
