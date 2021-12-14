INSERT INTO roles (role_id, role_name)
    VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');
INSERT INTO users (user_id, created, updated, email, username, password, person_id, status)
    VALUES (1, now(), now(), 'admin@gmail.com', 'admin', '$2a$04$3bZ4uZt9qjm0tmHkQ.WPEebcwNjzprO2RKQNlvc/vIwm5ETsXu8Ae', 1, 'ACTIVE'), (2, now(), now(), 'user@gmail.com', 'user', '$2a$04$4m33SMFgPwNwztWAZ/V/CeO/pT060mqHp5EPXWacjMpJ3XcBUu9ym', 2, 'ACTIVE');
INSERT INTO user_role(user_id, role_id)
    VALUES (1, 1), (2, 2);

SELECT pg_catalog.setval('roles_role_id_seq', 2, true);
SELECT pg_catalog.setval('users_user_id_seq', 2, true);