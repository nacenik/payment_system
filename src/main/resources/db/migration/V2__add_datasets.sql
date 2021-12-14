INSERT INTO persons (id, first_name, last_name)
    VALUES (1, 'Nikita', 'Oleksin'), (2, 'User', 'Userov');

INSERT INTO account_types (id, name)
    VALUES (1, 'simple/card'), (2, 'credit/card');

INSERT INTO accounts (id, account_number, balance, type_id, person_id)
    VALUES (1, '123123', 5540, 1, 1), (2, '12311111', 555, 2, 1), (3, '1213d12', 2222, 1, 2);

INSERT INTO payments (id, amount, reason, status, timestamp, source_id, destination_id)
    VALUES (1, 500, 'put to credit', 'ok', '2021-08-25 13:18:54', 2, 3), (2, 1000, 'get from credit', 'error','2021-08-25 13:19:00', 3, 2);

SELECT pg_catalog.setval('persons_id_seq', 2, true);
SELECT pg_catalog.setval('account_types_id_seq', 2, true);
SELECT pg_catalog.setval('accounts_id_seq', 3, true);
SELECT pg_catalog.setval('payments_id_seq', 2, true);