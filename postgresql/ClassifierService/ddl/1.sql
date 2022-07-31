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


CREATE DATABASE classifier WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Russian_Russia.1251';

\connect classifier

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

CREATE SCHEMA countries_and_categories;

SET default_tablespace = '';
SET default_table_access_method = heap;

CREATE TABLE countries_and_categories.country
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    title character varying,
    description text,
    PRIMARY KEY (uuid)
);

ALTER TABLE ONLY countries_and_categories.country
    ADD CONSTRAINT unique_title_key UNIQUE (title);

CREATE TABLE countries_and_categories.category
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    title character varying,
    PRIMARY KEY (uuid)
);

ALTER TABLE ONLY countries_and_categories.category
    ADD CONSTRAINT unique_titleCategory_key UNIQUE (title);
