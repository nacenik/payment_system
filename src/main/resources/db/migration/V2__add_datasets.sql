INSERT INTO persons (id, first_name, last_name)
    VALUES (1, 'Nikita', 'Oleksin');

INSERT INTO types (id, name)
    VALUES (1, 'simple/card'), (2, 'credit/card');

INSERT INTO accounts (id, account_number, balance, type_id, person_id)
    VALUES (2, '123123', 5540, 1, 1), (3, '12311111', 555, 2, 1);