CREATE DATABASE concerts
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE SCHEMA concert_events
    AUTHORIZATION postgres;

CREATE TABLE concert_events.concert
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
    category_uuid uuid,
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS concert_events.concert
    OWNER to postgres;



