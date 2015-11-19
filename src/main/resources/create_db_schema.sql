-- Role: peter

-- DROP ROLE peter;

CREATE ROLE peter LOGIN
  ENCRYPTED PASSWORD 'md54474b08c5d1bd42db77d8550c9620903'
  NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;

---------------------------------


-- Tablespace: peter_space

-- DROP TABLESPACE peter_space

CREATE TABLESPACE peter_space
  OWNER peter
  LOCATION 'C:\\postgres_tablespaces\\peter_space';
---------------------------------------------------


-- Database: peter_db

-- DROP DATABASE peter_db;

CREATE DATABASE peter_db
  WITH OWNER = peter
       ENCODING = 'UTF8'
       TABLESPACE = peter_space
       LC_COLLATE = 'English_United Kingdom.1252'
       LC_CTYPE = 'English_United Kingdom.1252'
       CONNECTION LIMIT = -1;

-------------------------------------
             -- Schema: public

-- DROP SCHEMA public;

CREATE SCHEMA public
  AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public
  IS 'standard public schema';
------------------------------------------

-- Schema: peters

-- DROP SCHEMA peters;

CREATE SCHEMA peters
  AUTHORIZATION peter;

---------------------------------------------
