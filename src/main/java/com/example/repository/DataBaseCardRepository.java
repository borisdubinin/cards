package com.example.repository;

import com.example.config.DatabaseConfig;
import com.example.exception.DatabaseOperationException;
import com.example.model.Card;
import com.example.model.CardStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseCardRepository implements CardRepository {

    @Override
    public Card insert(Card card) {
        String sql = """
                INSERT INTO cards (number, holderName, expirationDate, status, accountId)
                        VALUES (?, ?, ?, ?, ?)
                        RETURNING id, number, holderName, expirationDate, status, accountId, createdAt, updatedAt
                """;

        try (Connection conn = getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {

            query.setString(1, card.getNumber());
            query.setString(2, card.getHolderName());
            query.setString(3, card.getExpirationDate() == null ? null : card.getExpirationDate().toString());
            query.setString(4, card.getStatus() == null ? null : card.getStatus().toString());
            query.setObject(5, card.getAccountId(), Types.BIGINT);

            ResultSet rs = query.executeQuery();
            if (rs.next()) return parseResultSet(rs);
            else throw new SQLException("Insert did not return a row");

        } catch (SQLException e) {
            throw new DatabaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> update(Long id, Card card) {
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

            query.setString(1, card.getNumber());
            query.setString(2, card.getHolderName());
            query.setString(3, card.getExpirationDate() == null ? null : card.getExpirationDate().toString());
            query.setString(4, card.getStatus() == null ? null : card.getStatus().toString());
            query.setObject(5, card.getAccountId(), Types.BIGINT);
            query.setLong(6, id);

            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                return Optional.of(parseResultSet(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> getById(Long id) {
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
            throw new DatabaseOperationException(e.getMessage(), e);
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
            throw new DatabaseOperationException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> deleteById(Long id) {
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
            throw new DatabaseOperationException(e.getMessage(), e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getDataSource().getConnection();
    }

    private Card parseResultSet(ResultSet rs) throws SQLException {
        Card card = new Card();
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
}
