INSERT INTO persons (id, first_name, last_name)
    VALUES (1, 'Nikita', 'Oleksin');

INSERT INTO accounts (id, account_number, account_type, balance, person_id)
    VALUES (2, '123123', 'simpleCard', 5540, 1), (3, '12311111', 'creditCard', 555, 1);