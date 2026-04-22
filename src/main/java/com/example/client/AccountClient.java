package com.example.client;

import com.example.dto.AccountResponseDto;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface AccountClient {

    @RequestLine("GET /accounts")
    @Headers("Content-Type: application/json")
    List<AccountResponseDto> getAccounts();
}