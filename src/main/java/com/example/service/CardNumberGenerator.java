package com.example.service;

import java.util.concurrent.atomic.AtomicLong;

public class CardNumberGenerator {

    private static final AtomicLong sequence = new AtomicLong(1);

    public static String generateUniqueNumber() {
        long currentNumber = sequence.getAndIncrement();
        String strNumber = String.format("%016d", currentNumber);
        return formatCardNumber(strNumber);
    }

    private static String formatCardNumber(String rawNumber) {
        return String.format("%s-%s-%s-%s",
                rawNumber.substring(0, 4),
                rawNumber.substring(4, 8),
                rawNumber.substring(8, 12),
                rawNumber.substring(12, 16)
        );
    }
}
