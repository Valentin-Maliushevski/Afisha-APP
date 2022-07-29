CREATE DATABASE classifier
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE SCHEMA countries_and_categories
    AUTHORIZATION postgres;

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

ALTER TABLE IF EXISTS countries_and_categories.country
    OWNER to postgres;


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

ALTER TABLE IF EXISTS countries_and_categories.category
    OWNER to postgres;