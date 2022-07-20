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
    title character varying(10),
    description character varying(50),
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS countries_and_categories.country
    OWNER to postgres;


CREATE TABLE countries_and_categories.category
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    title character varying(50),
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS countries_and_categories.category
    OWNER to postgres;