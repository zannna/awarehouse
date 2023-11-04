TRUNCATE TABLE conferences, locations, speeches, speakers, speeches_speakers CASCADE;
ALTER SEQUENCE locations_id_seq RESTART WITH 1;
ALTER SEQUENCE speakers_id_seq RESTART WITH 1;
ALTER SEQUENCE speeches_id_seq RESTART WITH 1;