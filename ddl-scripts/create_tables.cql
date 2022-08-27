-- This CQL script will create the keyspace and all tables needed for the this sample.
-- It includes the messages and snapshot tables (write-side) and the projection tables (read-side).

-- NOTE: the keyspace as created here is probably not what you need in a production environment.
-- This is good enough for local development though.

CREATE KEYSPACE IF NOT EXISTS db
WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS db.weather_station (
  id uuid PRIMARY KEY,
  country TEXT,
  city TEXT,
  station_id TEXT,
  entry INT,
  dob DATE,
  value INT
);

insert into db.weather_station (id, country, city, station_id, entry, value) values(1, 'England', 'Newcastle', '123', 1, 0);

CREATE KEYSPACE IF NOT EXISTS smis
  WITH REPLICATION = { 'class' : 'SimpleStrategy','replication_factor':1 };


CREATE TABLE IF NOT EXISTS smis.messages (
  persistence_id text,
  partition_nr bigint,
  sequence_nr bigint,
  timestamp timeuuid,
  timebucket text,
  writer_uuid text,
  ser_id int,
  ser_manifest text,
  event_manifest text,
  event blob,
  meta_ser_id int,
  meta_ser_manifest text,
  meta blob,
  tags set<text>,
  PRIMARY KEY ((persistence_id, partition_nr), sequence_nr, timestamp));

CREATE TABLE IF NOT EXISTS smis.tag_views (
  tag_name text,
  persistence_id text,
  sequence_nr bigint,
  timebucket bigint,
  timestamp timeuuid,
  tag_pid_sequence_nr bigint,
  writer_uuid text,
  ser_id int,
  ser_manifest text,
  event_manifest text,
  event blob,
  meta_ser_id int,
  meta_ser_manifest text,
  meta blob,
  PRIMARY KEY ((tag_name, timebucket), timestamp, persistence_id, tag_pid_sequence_nr));

CREATE TABLE IF NOT EXISTS smis.tag_write_progress(
  persistence_id text,
  tag text,
  sequence_nr bigint,
  tag_pid_sequence_nr bigint,
  offset timeuuid,
  PRIMARY KEY (persistence_id, tag));

CREATE TABLE IF NOT EXISTS smis.tag_scanning(
  persistence_id text,
  sequence_nr bigint,
  PRIMARY KEY (persistence_id));

CREATE TABLE IF NOT EXISTS smis.metadata(
  persistence_id text PRIMARY KEY,
  deleted_to bigint,
  properties map<text,text>);

CREATE TABLE IF NOT EXISTS smis.all_persistence_ids(
  persistence_id text PRIMARY KEY);

CREATE TABLE IF NOT EXISTS smis.snapshots (
  persistence_id text,
  sequence_nr bigint,
  timestamp bigint,
  ser_id int,
  ser_manifest text,
  snapshot_data blob,
  snapshot blob,
  meta_ser_id int,
  meta_ser_manifest text,
  meta blob,
  PRIMARY KEY (persistence_id, sequence_nr))
  WITH CLUSTERING ORDER BY (sequence_nr DESC);

CREATE TABLE IF NOT EXISTS smis.offset_store (
  projection_name text,
  partition int,
  projection_key text,
  offset text,
  manifest text,
  last_updated timestamp,
  PRIMARY KEY ((projection_name, partition), projection_key));

CREATE TABLE IF NOT EXISTS smis.projection_management (
  projection_name text,
  partition int,
  projection_key text,
  paused boolean,
  last_updated timestamp,
  PRIMARY KEY ((projection_name, partition), projection_key));

CREATE TABLE IF NOT EXISTS smis.application_details (
  application_number text PRIMARY KEY,
  first_name text,
  surname text,
  dob Date,
  house_number Int,
  street_name text,
  city text,
  county text,
  post_code text,
  primary_guardian_name text,
  secondary_guardian_name text,
  primary_telephone_num text,
  secondary_telephone_num text
);
