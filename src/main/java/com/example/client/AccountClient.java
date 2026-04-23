package com.example.client;

import com.example.dto.AccountResponseDto;
import feign.RequestLine;

import java.util.List;

public interface AccountClient {

    @RequestLine("GET /accounts")
    List<AccountResponseDto> getAccounts();
}