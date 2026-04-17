package com.example.repository;

import com.example.model.Card;
import com.example.model.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Card> cardRowMapper = (rs, _) -> new Card(
            rs.getLong("id"),
            rs.getString("number"),
            rs.getString("holderName"),
            YearMonth.parse(rs.getString("expirationDate")),
            CardStatus.valueOf(rs.getString("status")),
            rs.getLong("accountId"),
            rs.getTimestamp("createdAt").toLocalDateTime(),
            Optional.ofNullable(rs.getTimestamp("updatedAt"))
                    .map(Timestamp::toLocalDateTime)
                    .orElse(null)
    );

    @Override
    public Optional<Card> getById(Long id) {
        String sql = "SELECT * FROM cards WHERE id = ?";
        return jdbcTemplate.query(sql, cardRowMapper, id)
                .stream()
                .findFirst();
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

        return jdbcTemplate.queryForObject(sql, cardRowMapper,
                card.getNumber(),
                card.getHolderName(),
                card.getExpirationDate() != null ? card.getExpirationDate().toString() : null,
                card.getStatus() != null ? card.getStatus().toString() : null,
                card.getAccountId()
        );
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

        return jdbcTemplate.query(sql, cardRowMapper,
                        card.getNumber(),
                        card.getHolderName(),
                        card.getExpirationDate() != null ? card.getExpirationDate().toString() : null,
                        card.getStatus() != null ? card.getStatus().toString() : null,
                        card.getAccountId(),
                        id
                ).stream()
                .findFirst();
    }

    @Override
    public Optional<Card> deleteById(Long id) {
        String sql = "DELETE FROM cards WHERE id = ? RETURNING *";
        return jdbcTemplate.query(sql, cardRowMapper, id)
                .stream()
                .findFirst();
    }
}