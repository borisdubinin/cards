ALTER TABLE cards
    RENAME COLUMN holderName TO holder_name;

ALTER TABLE cards
    RENAME COLUMN expirationDate TO expiration_date;

ALTER TABLE cards
    RENAME COLUMN createdAt TO created_at;

ALTER TABLE cards
    RENAME COLUMN updatedAt TO updated_at;