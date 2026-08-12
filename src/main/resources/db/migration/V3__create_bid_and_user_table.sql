CREATE TABLE users
(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL,

    CONSTRAINT chk_role_user CHECK ( role IN ('ADMIN', 'BIDDER') )
);

INSERT INTO users (id, name, email, role)
VALUES (gen_random_uuid(), 'Breno Guilherme Cauê Araújo', 'breno-araujo80@email.com', 'BIDDER'),
       (gen_random_uuid(), 'André Leandro Cavalcanti', 'andre-cavalcanti80@email.com', 'BIDDER'),
       (gen_random_uuid(), 'Marcos Davi Ian Dias', 'marcos.davi.dias@email.com', 'BIDDER'),
       (gen_random_uuid(), 'Hugo Lucca Rezende', 'hugo-rezende98@email.com', 'ADMIN');

CREATE TABLE bids
(
    id UUID PRIMARY KEY NOT NULL,
    lot_id UUID NOT NULL,
    user_id UUID NOT NULL,
    value NUMERIC(15,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_bd_lot FOREIGN KEY (lot_id) REFERENCES lots (id),
    CONSTRAINT fk_bd_user FOREIGN KEY (user_id) REFERENCES users (id)
);