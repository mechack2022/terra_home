package com.fragile.terra_home.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryAccountResponseDto {

    private String accountHolderName;
    private String bankName;
    private String accountNumber;
}
