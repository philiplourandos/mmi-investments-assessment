CREATE TYPE withdraw_status AS ENUM('STARTED', 'EXECUTING', 'DONE');
CREATE CAST (varchar AS withdraw_status) WITH INOUT AS IMPLICIT;

CREATE TABLE WITHDRAW(
    id BIGSERIAL PRIMARY KEY,
    client_product_id BIGINT NOT NULL,
    status withdraw_status NOT NULL,
    amount DECIMAL(20,2) NOT NULL);

CREATE TABLE WITHDRAW_AUDIT_TRACKING(
    id BIGSERIAL PRIMARY KEY,
    withdraw_id BIGINT NOT NULL,
    status withdraw_status NOT NULL,
    event_created TIMESTAMP DEFAULT NOW(),
    previous_balance DECIMAL(20,2) NOT NULL);

ALTER TABLE WITHDRAW ADD CONSTRAINT fk_client_product_id FOREIGN KEY (client_product_id) REFERENCES CLIENT_PRODUCTS(id);
ALTER TABLE WITHDRAW_AUDIT_TRACKING ADD CONSTRAINT fk_withdraw_id FOREIGN KEY (withdraw_id) REFERENCES WITHDRAW(id);