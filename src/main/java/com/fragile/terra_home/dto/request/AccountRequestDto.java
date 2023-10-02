package com.fragile.terra_home.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.aspectj.bridge.IMessage;
;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {
    @NotBlank(message="Account full name is require")
    private String accountHolderName;
    @NotBlank(message="Bank name is required")
    private String bankName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;
}
