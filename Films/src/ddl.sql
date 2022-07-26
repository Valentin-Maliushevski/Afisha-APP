CREATE DATABASE films
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;


CREATE SCHEMA film_events
    AUTHORIZATION postgres;


CREATE TABLE film_events.film
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    title character varying,
    description character varying,
    dt_event timestamp without time zone,
    dt_end_of_sale timestamp without time zone,
    event_status character varying,
    event_type character varying,
    country_uuid uuid,
    duration smallint,
    release_year smallint,
    release_date timestamp without time zone,
    author_uuid uuid,
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS film_events.film
    OWNER to postgres;
