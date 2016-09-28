-- // CLOUD-66305 added ranger config to cluster
-- Migration SQL that makes the change goes here.

ALTER TABLE cluster ADD COLUMN rangerconfig_id BIGINT REFERENCES rdsconfig(id);

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE cluster DROP COLUMN IF EXISTS rangerconfig_id;

