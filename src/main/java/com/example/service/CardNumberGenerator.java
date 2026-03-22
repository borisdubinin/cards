package com.example.service;

import java.util.concurrent.atomic.AtomicLong;

public class CardNumberGenerator {

    private static final String BIN = "900000";
    private static final AtomicLong sequence = new AtomicLong(0);

    public static String generateUniqueNumber() {
        long currentSequence = sequence.incrementAndGet();
        String seqPart = String.format("%09d", currentSequence);
        String partialNumber = BIN + seqPart;
        int checkDigit = calculateLuhnCheckDigit(partialNumber);
        String fullNumber = partialNumber + checkDigit;
        return formatCardNumber(fullNumber);
    }

    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean doubleDigit = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum * 9) % 10;
    }

    private static String formatCardNumber(String rawNumber) {
        return String.format("%s %s %s %s",
                rawNumber.substring(0, 4),
                rawNumber.substring(4, 8),
                rawNumber.substring(8, 12),
                rawNumber.substring(12, 16)
        );
    }
}
