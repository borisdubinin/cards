package com.example.repository;

import com.example.exception.DatabaseOperationException;
import com.example.model.Card;
import com.example.model.CardStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public class SpringJdbcCardRepository implements CardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Card> cardRowMapper = (rs, _) -> {
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
    };

    @Override
    public Optional<Card> getById(Long id) {
        String sql = "SELECT * FROM cards WHERE id = ?";

        try {
            Card card = jdbcTemplate.queryForObject(sql, cardRowMapper, id);
            return Optional.ofNullable(card);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Card> getAll() {
        String sql = "SELECT * FROM cards";
        return jdbcTemplate.query(sql, cardRowMapper);
    }

    @Override
    public Card insert(Card card) {
        String sql = """
                INSERT INTO cards (number, holderName, expirationDate, status, accountId)
                VALUES (?, ?, ?, ?, ?)
                RETURNING *
                """;

        try {
            return jdbcTemplate.queryForObject(sql, cardRowMapper,
                    card.getNumber(),
                    card.getHolderName(),
                    card.getExpirationDate() != null ? card.getExpirationDate().toString() : null,
                    card.getStatus() != null ? card.getStatus().toString() : null,
                    card.getAccountId()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseOperationException("Insert failed, no row returned", e);
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
                RETURNING *
                """;

        try {
            Card updatedCard = jdbcTemplate.queryForObject(sql, cardRowMapper,
                    card.getNumber(),
                    card.getHolderName(),
                    card.getExpirationDate() != null ? card.getExpirationDate().toString() : null,
                    card.getStatus() != null ? card.getStatus().toString() : null,
                    card.getAccountId(),
                    id
            );
            return Optional.ofNullable(updatedCard);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Card> deleteById(Long id) {
        String sql = "DELETE FROM cards WHERE id = ? RETURNING *";

        try {
            Card deletedCard = jdbcTemplate.queryForObject(sql, cardRowMapper, id);
            return Optional.ofNullable(deletedCard);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}