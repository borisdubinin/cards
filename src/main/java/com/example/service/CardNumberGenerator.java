package com.example.service;

public class CardNumberGenerator {

    public static String generateRandomNumber() {
        return String.format("%04d-%04d-%04d-%04d",
                (int) (Math.random() * 10000),
                (int) (Math.random() * 10000),
                (int) (Math.random() * 10000),
                (int) (Math.random() * 10000));
    }
}