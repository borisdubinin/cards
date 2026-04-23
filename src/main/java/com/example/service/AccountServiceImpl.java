package com.example.service;

import com.example.client.AccountClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountClient accountClient;

    @Override
    public boolean exists(Long id) {
        return accountClient.getAccounts().stream()
                .anyMatch(account -> Objects.equals(account.getId(), id));
    }
}
