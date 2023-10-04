-- valid user data
INSERT INTO CLIENTS(id, client_name, mobile_number, email, address, date_of_birth)
    VALUES(2600, 'Philip Lourandos', '0835550001', 'philip.lourandos@gmail.com', '235 Beach Rd, Sea Point, Cape Town, 8005', '1978-06-25');
INSERT INTO CLIENTS(id, client_name, mobile_number, email, address, date_of_birth)
    VALUES(6667, 'James Pereria', '0835550002', 'james@gmail.com', '16 Marmion Rd, Oranjezicht, Cape Town, 8005', '1958-05-30');

-- product data
INSERT INTO FINANCIAL_PRODUCTS(name, type) VALUES('Local Unit Trusts', 'INVESTMENTS');
INSERT INTO FINANCIAL_PRODUCTS(name, type) VALUES('Offshore Unit Trusts', 'INVESTMENTS');
INSERT INTO FINANCIAL_PRODUCTS(name, type) VALUES('Momentum Money', 'SAVINGS');
INSERT INTO FINANCIAL_PRODUCTS(name, type) VALUES('Momentum RA', 'RETIREMENT');

-- client linked products
INSERT INTO CLIENT_PRODUCTS(client_id, financial_product_id, balance) 
    VALUES((SELECT id FROM CLIENTS WHERE email = 'philip.lourandos@gmail.com'),
           (SELECT id FROM FINANCIAL_PRODUCTS WHERE name = 'Momentum RA'),
           500000.00);
INSERT INTO CLIENT_PRODUCTS(client_id, financial_product_id, balance) 
    VALUES((SELECT id FROM CLIENTS WHERE email = 'philip.lourandos@gmail.com'),
           (SELECT id FROM FINANCIAL_PRODUCTS WHERE name = 'Momentum Money'),
           36000.00);