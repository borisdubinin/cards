package com.example.repository;

import com.example.exception.DataConstraintViolationException;
import com.example.exception.DataSourceConnectionException;
import com.example.model.Card;
import com.example.model.CardData;
import com.example.model.CardStatus;

import org.flywaydb.core.Flyway;

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

    static {
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseConfig.getDataSource())
                .load();

        flyway.migrate();
    }

    @Override
    public Card insert(CardData cardData) {
        String sql = """
                INSERT INTO cards (number, holderName, expirationDate, status, accountId)
                        VALUES (?, ?, ?, ?, ?)
                        RETURNING id, number, holderName, expirationDate, status, accountId, createdAt, updatedAt
                """;

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            query.setString(1, cardData.getNumber());
            query.setString(2, cardData.getHolderName());
            query.setString(3, cardData.getExpirationDate().toString());
            query.setString(4, cardData.getStatus().name());
            query.setLong(5, cardData.getAccountId());

            ResultSet rs = query.executeQuery();
            rs.next();

            return parseResultSet(rs);

        } catch (SQLException e) {
            throwInformativeException(e);
            return null;
        }
    }

    @Override
    public Card update(Long id, CardData cardData) {
        String sql = """
                UPDATE cards SET
                    number = COALESCE(?, number),
                    holderName = COALESCE(?, holderName),
                    expirationDate = COALESCE(?, expirationDate),
                    status = COALESCE(?, status),
                    accountId = COALESCE(?, accountId),
                    updatedAt = CURRENT_TIMESTAMP
                WHERE id = ?
                RETURNING id, number, holderName, expirationDate, status, accountId, createdAt, updatedAt
                """;

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            query.setString(1, cardData.getNumber());
            query.setString(2, cardData.getHolderName());
            query.setString(3, cardData.getExpirationDate().toString());
            query.setString(4, cardData.getStatus().name());
            query.setLong(5, cardData.getAccountId());
            query.setLong(6, id);

            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                return parseResultSet(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throwInformativeException(e);
            return null;
        }
    }

    @Override
    public Optional<Card> get(Long id) {
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
                Card card = parseResultSet(rs);
                return Optional.of(card);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throwInformativeException(e);
            return null;
        }
    }

    @Override
    public List<Card> getAll() {
        String sql = """
                SELECT *
                FROM cards
                """;

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            ResultSet rs = query.executeQuery();
            List<Card> cards = new ArrayList<>();

            while (rs.next()) {
                Card card = parseResultSet(rs);
                cards.add(card);
            }
            return cards;

        } catch (SQLException e) {
            throwInformativeException(e);
            return null;
        }
    }

    @Override
    public Optional<Card> delete(Long id) {
        String sql = """
                DELETE
                FROM cards
                WHERE id = ?
                RETURNING id, number, holderName, expirationDate, status, accountId, createdAt, updatedAt
                """;

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            query.setLong(1, id);
            ResultSet rs = query.executeQuery();

            if (rs.next()) {
                Card card = parseResultSet(rs);
                return Optional.of(card);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throwInformativeException(e);
            return null;
        }
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getDataSource().getConnection();
    }

    private Card parseResultSet(ResultSet rs) throws SQLException {
        Card card = Card.builder().build();
        card.setId(rs.getLong("id"));
        card.setNumber(rs.getString("number"));
        card.setHolderName(rs.getString("holderName"));
        card.setExpirationDate(YearMonth.parse(rs.getString("expirationDate")));
        card.setStatus(CardStatus.valueOf(rs.getString("status")));
        card.setAccountId(rs.getLong("accountId"));
        card.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        card.setUpdatedAt(Optional.ofNullable(rs.getTimestamp("updatedAt"))
                .map(Timestamp::toLocalDateTime)
                .orElse(null));
        return card;
    }

    private static void throwInformativeException(SQLException e) {
        switch(e.getSQLState().substring(0,1)) {
            case "08" -> throw new DataSourceConnectionException(e.getMessage(), e);
            case "23" -> throw new DataConstraintViolationException(e.getMessage(), e);
            default -> throw new RuntimeException(e.getMessage());
        }
    }
}
