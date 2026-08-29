CREATE TABLE auctions
(
    id          UUID PRIMARY KEY         NOT NULL,
    title       VARCHAR(150)             NOT NULL,
    description VARCHAR(255),
    principal   VARCHAR(150)             NOT NULL,
    start_date  TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    status      VARCHAR(20)              NOT NULL,

    CONSTRAINT chk_status_auction CHECK (status IN ('SCHEDULED', 'ACTIVE', 'CLOSED'))
);