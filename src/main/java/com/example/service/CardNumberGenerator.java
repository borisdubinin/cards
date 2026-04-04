package com.example.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicLong;

public class CardNumberGenerator {

    private static final Path STATE_PATH = Paths.get(
            System.getProperty("user.home"), ".cards", "card_number.state");
    private static final AtomicLong sequence = new AtomicLong();

    static {
        createFileIfNotExists();
        loadLastValue();
        addShutdownHook();
    }

    public static String generateUniqueNumber() {
        long currentNumber = sequence.incrementAndGet();
        String strNumber = String.format("%016d", currentNumber);
        return formatCardNumber(strNumber);
    }

    private static void createFileIfNotExists() {
        try {
            Files.createDirectories(STATE_PATH.getParent());
            if (!Files.exists(STATE_PATH)) {
                Files.createFile(STATE_PATH);
                Files.writeString(STATE_PATH, "0");
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания файла: " + e.getMessage());
        }
    }

    private static void loadLastValue() {
        try {
            if (Files.exists(STATE_PATH)) {
                String content = new String(Files.readAllBytes(STATE_PATH)).trim();
                long lastValue = Long.parseLong(content);
                sequence.set(lastValue);
                return;
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки состояния: " + e.getMessage());
        }
        sequence.set(1);
    }

    private static void addShutdownHook() {
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> saveLastValue(sequence.get())));
    }

    private static void saveLastValue(long value) {
        try {
            Files.write(STATE_PATH, String.valueOf(value).getBytes());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения состояния: " + e.getMessage());
        }
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