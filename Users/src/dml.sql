INSERT INTO users.t_role(id, name)
VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO users.t_user (uuid, dt_create, dt_update, username, nick, user_status, passwrd,account_non_expired,
account_non_locked, credentials_non_expired, enabled)
VALUES (gen_random_uuid (), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
'admin@gmail.com', 'admin', 'ACTIVATED',
'$2a$10$P/8v2L.I4agnxE16OorfpOWTlsyPFAbvwZnX2eApDZTGJAHowyX32','true', 'true', 'true', 'true');

INSERT INTO users.t_user_roles (user_uuid, role_id)
VALUES((SELECT uuid  FROM users.t_user WHERE nick = 'admin'), 2);

