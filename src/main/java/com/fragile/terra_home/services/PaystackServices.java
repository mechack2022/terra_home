package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;

public interface PaystackServices {

    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);
}
