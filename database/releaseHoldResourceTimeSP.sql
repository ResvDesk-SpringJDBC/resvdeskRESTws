
SET GLOBAL event_scheduler = OFF;
SET GLOBAL event_scheduler = ON;

DELIMITER $$
DROP EVENT IF EXISTS `release_hold_resourcetime_event`$$

DELIMITER $$
  CREATE EVENT release_hold_resourcetime_event
  ON SCHEDULE EVERY 5 MINUTE
  COMMENT 'release hold resource time each 5 minute.'
  DO
    BEGIN   
      CALL release_holdresource_time();
    END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `release_holdresource_time`$$

DELIMITER $$
CREATE PROCEDURE `release_holdresource_time`()
BEGIN
	DECLARE sp_schedule_id bigint(20) unsigned;
	DECLARE done INT DEFAULT 0;
	DECLARE cur1 CURSOR FOR	SELECT id FROM schedule where status=1 and timestamp <= ADDTIME(now(), '-00:10') and timestamp > ADDTIME(now(), '-24:00');
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	OPEN cur1;
	REPEAT
		FETCH cur1 INTO sp_schedule_id;
		IF NOT done THEN
			UPDATE schedule SET status = 2 WHERE id = sp_schedule_id;
			UPDATE seat SET schedule_id = 0, reserved = 'N' WHERE schedule_id = sp_schedule_id;
			COMMIT;
		END IF;
	UNTIL done END REPEAT;
	CLOSE cur1;
	COMMIT;
END$$
DELIMITER ;