-- // instance metadata type for primary gateway
-- Migration SQL that makes the change goes here.

ALTER TABLE instancemetadata ADD COLUMN instanceMetadataType VARCHAR(255);
UPDATE instancemetadata SET instancemetadatatype='GATEWAY_PRIMARY' WHERE  instancegroup_id IN (SELECT id FROM instancegroup WHERE instancegrouptype='GATEWAY');
UPDATE instancemetadata SET instancemetadatatype='CORE' WHERE instancegroup_id NOT IN (SELECT id FROM instancegroup WHERE instancegrouptype='GATEWAY');

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE instancemetadata DROP COLUMN instancemetadatatype;
