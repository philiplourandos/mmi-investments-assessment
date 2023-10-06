CREATE TYPE withdraw_status AS ENUM('STARTED', 'EXECUTING', 'DONE');

CREATE TABLE withdraw(
    id BIGSERIAL PRIMARY KEY,
    client_product_id BIGINT NOT NULL,
    status withdraw_status NOT NULL DEFAULT 'STARTED',
    amount DECIMAL(20,2) NOT NULL);
    
CREATE TABLE withdraw_audit_tracking(
    id BIGSERIAL PRIMARY KEY,
    withdraw_id BIGINT NOT NULL,
    status withdraw_status NOT NULL,
    event_created TIMESTAMP DEFAULT NOW(),
    previous_balance DECIMAL(20,2) NOT NULL;
