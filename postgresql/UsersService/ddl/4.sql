SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE DATABASE users_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Russian_Russia.1251';

\connect users_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA users;

SET default_tablespace = '';
SET default_table_access_method = heap;

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
    enabled boolean
);

ALTER TABLE ONLY users.t_user
    ADD CONSTRAINT user_pkey PRIMARY KEY (uuid);

ALTER TABLE ONLY users.t_user
    ADD CONSTRAINT unique_username_key UNIQUE (username);

ALTER TABLE ONLY users.t_user
    ADD CONSTRAINT unique_nick_key UNIQUE (nick);


CREATE TABLE users.t_role
(
    id bigint,
    name character varying
);

ALTER TABLE ONLY users.t_role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


CREATE TABLE users.t_user_roles
(
    user_uuid uuid,
    role_id integer
);

ALTER TABLE ONLY users.t_user_roles
    ADD CONSTRAINT role FOREIGN KEY (role_id) REFERENCES users.t_role(id);
