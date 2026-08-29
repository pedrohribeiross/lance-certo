CREATE TABLE categories
(
    id   UUID PRIMARY KEY    NOT NULL,
    name VARCHAR(150) UNIQUE NOT NULL
);

INSERT INTO categories (id, name)
VALUES (gen_random_uuid(), 'Imóveis'),
       (gen_random_uuid(), 'Veículos'),
       (gen_random_uuid(), 'Outros');

CREATE TABLE lots
(
    id            UUID PRIMARY KEY NOT NULL,
    auction_id    UUID             NOT NULL,
    category_id   UUID             NOT NULL,
    description   VARCHAR(255)     NOT NULL,
    starting_bid  NUMERIC(15, 2)   NOT NULL,
    current_value NUMERIC(15, 2)   NOT NULL,
    status        VARCHAR(20)      NOT NULL,

    CONSTRAINT chk_status_lot CHECK ( status IN ('AVAILABLE', 'AWARDED', 'SUSPENDED') ),
    CONSTRAINT fk_lt_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_lt_auction FOREIGN KEY (auction_id) REFERENCES auctions (id)
);