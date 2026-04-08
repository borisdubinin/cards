package com.example.utils;

import java.util.Random;

public class CardNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generateRandomNumber() {
        return String.format("%04d-%04d-%04d-%04d",
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000));
    }
}