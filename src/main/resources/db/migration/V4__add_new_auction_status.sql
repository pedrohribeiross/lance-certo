ALTER TABLE auctions DROP CONSTRAINT chk_status_auction;
ALTER TABLE auctions ADD CONSTRAINT chk_status_auction CHECK ( status IN ('SCHEDULED','ACTIVE','CLOSED','CANCELLED'));