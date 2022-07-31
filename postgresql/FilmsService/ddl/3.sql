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

CREATE DATABASE films WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Russian_Russia.1251';

\connect films

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

CREATE SCHEMA film_events;

SET default_tablespace = '';
SET default_table_access_method = heap;

CREATE TABLE film_events.film
(
    uuid uuid,
    dt_create timestamp without time zone,
    dt_update timestamp without time zone,
    title character varying,
    description text,
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
