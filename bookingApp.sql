SELECT current_database();
ALTER USER bookingAdmin WITH PASSWORD 'bookingadmin';
ALTER USER bookingAdmin RENAME TO bookingadmin;
--Granting the role the permission to access the database --
GRANT CONNECT ON DATABASE booking_app_db TO bookingAdmin;

--CREATING THE SCHEMA--
CREATE SCHEMA booking_schema;

--GRANT ACCESS AND MODIFICATION OF SCHEMA--
GRANT CREATE ON SCHEMA booking_schema TO bookingAdmin;
GRANT USAGE ON SCHEMA booking_schema TO bookingAdmin;

--GRANT PERMISSIONS TO ACCESS AND MODIFY ALL OF THE ROLES--
GRANT SELECT, INSERT,UPDATE, DELETE ON ALL TABLES IN SCHEMA booking_schema TO bookingAdmin;

--SET THE DEFAULT SCHEMA TO THIS SCHEMA OR ELSE YOU WILL HAVE TO MODIFY APPLICATION PROPERTIES--
ALTER USER bookingAdmin SET search_path TO booking_schema;

--ALLOW THE ROLE TO CREATE MORE TABLES IF NEEDED IN THE FUTURE--
ALTER DEFAULT PRIVILEGES IN SCHEMA booking_schema
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLES TO bookingAdmin;

--SELECTING FROM TABLES --
SELECT * FROM booking_schema.driver;
SELECT * FROM booking_schema.ride;

--DELETE ALL ENTRIES FROM TABLES--
DELETE FROM booking_schema.driver;
DELETE FROM booking_schema.ride;

--DROP TABLES ENTIRELY--
DROP TABLE booking_schema.driver;
DROP TABLE booking_schema.ride;