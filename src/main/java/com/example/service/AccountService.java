package com.example.service;

public interface AccountService {

    /**
     *
     * @param iban IBAN of the account
     * @return true, if account with such id exists, false otherwise
     */
    boolean exists(String iban);
}
