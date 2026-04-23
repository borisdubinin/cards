package com.example.service;

public interface AccountService {

    /**
     *
     * @param id id of the account
     * @return true, if account with such id exists, false otherwise
     */
    boolean exists(Long id);
}
