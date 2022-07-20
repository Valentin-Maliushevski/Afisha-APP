
CREATE DATABASE users_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE SCHEMA users
    AUTHORIZATION postgres;

CREATE TABLE users.t_user
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    username character varying,
    nick character varying,
    user_status character varying,
    passwrd character varying,
    account_non_expired boolean,
    account_non_locked boolean,
    credentials_non_expired boolean,
    enabled boolean,
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS users.t_user
    OWNER to postgres;


CREATE TABLE users.t_role
(
    id bigint,
    name character varying,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS users.t_role
    OWNER to postgres;


CREATE TABLE users.t_user_roles
(
    uuid uuid,
    role_id integer
);

ALTER TABLE IF EXISTS users.t_user_roles
    OWNER to postgres;

