package com.fragile.terra_home.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitializePaymentResponse {

    private boolean status;
    private String message;
    private Data data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Component
    @Getter
    @Setter
    public static class Data {
        private String authorization_url;
        private String access_code;
        private String reference;
    }
}

