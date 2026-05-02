package com.example.repository;

import com.example.entity.CardEntity;
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

    private final RowMapper<CardEntity> cardRowMapper = (rs, _) -> new CardEntity(
            rs.getLong("id"),
            rs.getString("number"),
            rs.getString("holder_name"),
            YearMonth.parse(rs.getString("expiration_date")),
            CardStatus.valueOf(rs.getString("status")),
            rs.getString("iban"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            Optional.ofNullable(rs.getTimestamp("updated_at"))
                    .map(Timestamp::toLocalDateTime)
                    .orElse(null)
    );

    @Override
    public Optional<CardEntity> getById(Long id) {
        String sql = "SELECT * FROM cards WHERE id = ?";
        return jdbcTemplate.query(sql, cardRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<CardEntity> getAll() {
        String sql = "SELECT * FROM cards";
        return jdbcTemplate.query(sql, cardRowMapper);
    }

    @Override
    public CardEntity insert(CardEntity cardEntity) {
        String sql = """
                INSERT INTO cards (number, holder_name, expiration_date, status, iban)
                VALUES (?, ?, ?, ?, ?)
                RETURNING *
                """;

        return jdbcTemplate.queryForObject(sql, cardRowMapper,
                cardEntity.getNumber(),
                cardEntity.getHolderName(),
                cardEntity.getExpirationDate() != null ? cardEntity.getExpirationDate().toString() : null,
                cardEntity.getStatus() != null ? cardEntity.getStatus().toString() : null,
                cardEntity.getIban()
        );
    }

    @Override
    public Optional<CardEntity> update(Long id, CardEntity cardEntity) {
        String sql = """
                UPDATE cards SET
                    number = COALESCE(?, number),
                    holder_name = COALESCE(?, holder_name),
                    expiration_date = COALESCE(?, expiration_date),
                    status = COALESCE(?, status),
                    iban = COALESCE(?, iban),
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                RETURNING *
                """;

        return jdbcTemplate.query(sql, cardRowMapper,
                        cardEntity.getNumber(),
                        cardEntity.getHolderName(),
                        cardEntity.getExpirationDate() != null ? cardEntity.getExpirationDate().toString() : null,
                        cardEntity.getStatus() != null ? cardEntity.getStatus().toString() : null,
                        cardEntity.getIban(),
                        id
                ).stream()
                .findFirst();
    }

    @Override
    public Optional<CardEntity> deleteById(Long id) {
        String sql = "DELETE FROM cards WHERE id = ? RETURNING *";
        return jdbcTemplate.query(sql, cardRowMapper, id)
                .stream()
                .findFirst();
    }
}