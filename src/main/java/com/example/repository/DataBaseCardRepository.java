package com.example.repository;

import com.example.model.Card;
import com.example.model.CardStatus;

import org.flywaydb.core.Flyway;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseCardRepository implements CardRepository {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cardsdb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        dataSource = new HikariDataSource(config);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();
    }

    @Override
    public Card save(Card card) {
        return card.getId() == null
                ? insert(card)
                : update(card);
    }

    @Override
    public Optional<Card> getById(Long id) {
        return select(id);
    }

    @Override
    public List<Card> getAll() {
        return select();
    }

    @Override
    public Optional<Card> deleteById(Long id) {
        return delete(id);
    }

    private Card insert(Card card) {
        String sql = """
                INSERT INTO cards (number, holderName, expirationDate, status, accountId)
                        VALUES (?, ?, ?, ?, ?)
                        RETURNING id, createdAt
                """;
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, card.getNumber());
            query.setString(2, card.getHolderName());
            query.setString(3, card.getExpirationDate().toString());
            query.setString(4, card.getStatus().name());
            query.setLong(5, card.getAccountId());
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                card.setId(rs.getLong("id"));
                card.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
            }
        } catch (SQLException e) {
            Exception en = e;
        }
        return card;
    }

    private Card update(Card card) {
        String sql = """
                UPDATE cards
                SET number = ?, holderName = ?, expirationDate = ?, status = ?, accountId = ?, updatedAt = CURRENT_TIMESTAMP
                WHERE id = ?
                RETURNING id, createdAt, updatedAt
                """;
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, card.getNumber());
            query.setString(2, card.getHolderName());
            query.setString(3, card.getExpirationDate().toString());
            query.setString(4, card.getStatus().name());
            query.setLong(5, card.getAccountId());
            query.setLong(6, card.getId());
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                card.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                card.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }
        } catch (SQLException e) {

        }
        return card;
    }

    private Optional<Card> select(Long id) {
        String sql = """
                SELECT *
                FROM cards
                WHERE id = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setLong(1, id);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                Card card = Card.builder()
                        .id(rs.getLong("id"))
                        .number(rs.getString("number"))
                        .holderName(rs.getString("holderName"))
                        .expirationDate(YearMonth.parse(rs.getString("expirationDate")))
                        .status(CardStatus.valueOf(rs.getString("status")))
                        .accountId(rs.getLong("accountId"))
                        .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                        .updatedAt(Optional.ofNullable(rs.getTimestamp("updatedAt"))
                                .map(Timestamp::toLocalDateTime)
                                .orElse(null))
                        .build();
                return Optional.of(card);
            }
        } catch (SQLException _) {

        }
        return Optional.empty();
    }

    private List<Card> select() {
        String sql = """
                SELECT *
                FROM cards
                """;
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            ResultSet rs = query.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (rs.next()) {
                Card card = Card.builder()
                        .id(rs.getLong("id"))
                        .number(rs.getString("number"))
                        .holderName(rs.getString("holderName"))
                        .expirationDate(YearMonth.parse(rs.getString("expirationDate")))
                        .status(CardStatus.valueOf(rs.getString("status")))
                        .accountId(rs.getLong("accountId"))
                        .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                        .updatedAt(Optional.ofNullable(rs.getTimestamp("updatedAt"))
                                .map(Timestamp::toLocalDateTime)
                                .orElse(null))
                        .build();
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            Exception en = e;
        }
        return List.of();
    }

    private Optional<Card> delete(Long id) {
        String sql = """
                DELETE
                FROM cards
                WHERE id = ?
                RETURNING id, number, holdername, expirationdate, status, accountid, createdat, updatedat
                """;
        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setLong(1, id);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                Card card = Card.builder()
                        .id(rs.getLong("id"))
                        .number(rs.getString("number"))
                        .holderName(rs.getString("holderName"))
                        .expirationDate(YearMonth.parse(rs.getString("expirationDate")))
                        .status(CardStatus.valueOf(rs.getString("status")))
                        .accountId(rs.getLong("accountId"))
                        .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                        .updatedAt(Optional.ofNullable(rs.getTimestamp("updatedAt"))
                                .map(Timestamp::toLocalDateTime)
                                .orElse(null))
                        .build();
                return Optional.of(card);
            }
        } catch (SQLException _) {

        }
        return Optional.empty();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
