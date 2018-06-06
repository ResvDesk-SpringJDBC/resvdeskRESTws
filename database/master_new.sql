CREATE DATABASE  IF NOT EXISTS `master_new2`;
USE `master_new2`;

DROP TABLE IF EXISTS `admin_login_config`;
CREATE TABLE `admin_login_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `client_id` int(11) NOT NULL COMMENT 'Client ID',
  `password_expire_days` int(10) NOT NULL DEFAULT '-1' COMMENT 'days after the password will expire; -1 means it will never expire',
  `password_expiry_warning_days` int(10) NOT NULL DEFAULT '-1' COMMENT 'days before password expiry date to give warning alerts; -1 means it will never give expiry alerts',
  `password_reset_algorithm` varchar(1000) DEFAULT NULL,
  `password_reset_method` varchar(10) DEFAULT 'SMS' COMMENT 'EMAIL - reset by Email, SMS - reset by SMS, BOTH - reset by both',
  `password_reset_temp_storage_hours` int(10) NOT NULL DEFAULT '1',
  `password_complexity` varchar(1000) DEFAULT NULL COMMENT '6min,10max,1upper,1lower,1digit,1letter,1splchar,no_username',
  `user_restrict_ips` varchar(100) DEFAULT NULL COMMENT 'list of ip address in csv format to allow the user access',
  `max_wrong_login_attempts` int(10) DEFAULT '-1',
  `max_wrong_login_lock_reset_mins` int(10) DEFAULT '-1',
  `placeholder1` varchar(500) DEFAULT NULL,
  `placeholder2` varchar(500) DEFAULT NULL,
  `placeholder3` varchar(500) DEFAULT NULL,
  `placeholder4` varchar(500) DEFAULT NULL,
  `placeholder5` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `admin_login_new`;
CREATE TABLE `admin_login_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `client_id` int(11) NOT NULL COMMENT 'Client ID',
  `username` varchar(30) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Admin Login Username',
  `password` varchar(100) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Admin Login Password (encrypted)',
  `first_name` varchar(30) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'First Name',
  `last_name` varchar(30) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Last Name',
  `contact_phone` varchar(15) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Contact Phone',
  `contact_email` varchar(80) CHARACTER SET latin1 NOT NULL COMMENT 'Contact Email',
  `location_ids` varchar(100) CHARACTER SET latin1 DEFAULT '' COMMENT 'Location Privilege - Location ID''s which are assigned to this user',
  `resource_ids` varchar(100) CHARACTER SET latin1 DEFAULT '' COMMENT 'Resource Privilege - Resource ID''s which are assigned to this user',
  `start_date` datetime NOT NULL COMMENT 'User Start Date',
  `expire_date` datetime NOT NULL DEFAULT '2020-01-01 00:00:00',
  `suspend` char(1) NOT NULL DEFAULT 'N',
  `access_level` varchar(50) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Access Level for this user. example:SUPER,ADMIN,MANAGER,LOCATION,RESOURCE,SCHEDULER,SCHEDULER_LOCATION,SCHEDULER_RESOURCE,READONLY',
  `password_last_update_date` date DEFAULT NULL,
  `password_reset_security_question_1` varchar(1000) DEFAULT NULL,
  `password_reset_security_answer_1` varchar(1000) DEFAULT NULL,
  `password_reset_security_question_2` varchar(1000) DEFAULT NULL,
  `password_reset_security_answer_2` varchar(1000) DEFAULT NULL,
  `password_reset_security_question_3` varchar(1000) DEFAULT NULL,
  `password_reset_security_answer_3` varchar(1000) DEFAULT NULL,
  `password_reset_algorithm` varchar(100) DEFAULT NULL,
  `password_reset_sms_phone` varchar(10) DEFAULT NULL,
  `wrong_login_max_attempt_locked` char(1) DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `client_id` (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_login_reset_request`;
CREATE TABLE `admin_login_reset_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `login_id` int(11) NOT NULL,
  `sent_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expire_timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `temp_password` varchar(100) DEFAULT NULL,
  `email_reset_url` varchar(2000) DEFAULT NULL,
  `sms_reset_code` varchar(20) DEFAULT NULL,
  `sms_phone` varchar(10) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `reset_password_token` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `login_id` (`login_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `auto_pay`;
CREATE TABLE `auto_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `client_id` int(11) DEFAULT NULL COMMENT 'Client ID',
  `payment_engine` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Payment Engine. values: p = paypal, a = authorize.net etc',
  `display` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Display AutoPay button in admin site? values: Y or N',
  `status` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Status - not yet used',
  `max_amount` varchar(10) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Maximum Amount which can be billed',
  PRIMARY KEY (`id`),
  KEY `FK_auto_pay_client_id` (`client_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of when the record was created',
  `client_name` varchar(100) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Client Name',
  `client_code` varchar(8) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Unique Client Code - 8 character',
  `contact_person` varchar(60) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Contact Person',
  `contact_phone` varchar(15) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Contact Phone',
  `contact_email` varchar(80) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Contact Email',
  `website` varchar(200) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Business Website',
  `phone_transfer_number` varchar(15) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Phone number which will be transferred to our IVR server',
  `fax` varchar(200) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Business Fax',
  `address` varchar(80) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Address Line1',
  `address2` varchar(80) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Address Line2',
  `city` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'City',
  `state` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'State/Province',
  `zip` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Zip/Postal Code',
  `country` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Country',
  `appt_link` varchar(200) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Appointment Scheduler Link',
  `forward_url` varchar(200) CHARACTER SET latin1 DEFAULT NULL COMMENT 'website which will link to our online scheduler',
  `business_type` varchar(80) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Business Type',
  `appcode` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Application Code - apptdesk, reminddesk, labresultdesk, resvdesk',
  `db_server` varchar(100) DEFAULT NULL COMMENT 'Database Server Address or IP Address. example: www.itfrontdesk.com, proddb1.itfrontdesk.com, proddb2.itfrontdesk.com etc',
  `db_name` varchar(25) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'database name',
  `license_key` varchar(500) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'License Key meant for appliance',
  `date_subscribed` date NOT NULL COMMENT 'Date of subscription',
  `date_cancelled` date DEFAULT NULL COMMENT 'Date of cancellation',
  `delete_flag` char(1) CHARACTER SET latin1 DEFAULT 'N' COMMENT 'Soft Delete Flag. Y = Delete. N = Active',
  `active` char(1) CHARACTER SET latin1 DEFAULT 'Y' COMMENT 'Is account active? values: Y or N',
  `locked` char(1) CHARACTER SET latin1 DEFAULT 'N' COMMENT 'Is account locked? values: Y or N',
  `group_account` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Group Account Code. Example: catapult, bci etc',
  `channel_partner_id` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Channel Partner ID - not yet used',
  `sales_rep` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Sales Rep for this account',
  `billable` char(1) CHARACTER SET latin1 DEFAULT 'N' COMMENT 'Billable Client? Y = yes, N = no',
  `allow_online_appt` char(1) DEFAULT 'N',
  `allow_ivr_appt` char(1) DEFAULT 'N',
  `allow_mobile_appt` char(1) DEFAULT 'N',
  `allow_mobile_admin` char(1) DEFAULT 'N',
  `allow_ivr_notify` char(1) DEFAULT 'N',
  `allow_sms_notify` char(1) DEFAULT 'N',
  `allow_email_notify` char(1) DEFAULT 'N',
  `cache_enabled` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_code` (`client_code`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `client_deployment_config`;
CREATE TABLE `client_deployment_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `client_id` int(11) NOT NULL COMMENT 'Client ID',
  `block_time_in_mins` int(11) NOT NULL COMMENT 'Minimum Block Time for calendar display - may be obsolete',
  `resource_calendar_months` tinyint(4) NOT NULL COMMENT 'How many months calendar is required?',
  `user_expiry_date_time` datetime NOT NULL COMMENT 'Account Expiry Date',
  `day_start_time` time NOT NULL COMMENT 'Day hour start time for calendar display - may be obsolete',
  `day_end_time` time NOT NULL COMMENT 'Day hour end time for calendar display - may be obsolete',
  `integrated_reminddesk` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Integrated ReminderDesl? values: Y or N',
  `server_flag` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Is it Server? values: Y or N',
  `auto_file_upload` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Auto File Upload Feature Enabled?  - may be obsolete',
  `same_multiloc_resource` char(1) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Same Resource works in multiple Locations? values: Y or N - may be obsolete',
  `time_zone` varchar(100) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT 'Time Zone in Java format',
  `date_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Date Format',
  `date_yyyy_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Date Year Format',
  `full_date_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Full Date Format',
  `full_datetime_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Full Date Time Format',
  `popup_calendardate_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Format for Pop-up Calendar',
  `time_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Time Format',
  `time_withsec_format` varchar(30) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Time with Seconds format',
  `phone_format` varchar(10) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Phone Format',
  `country_code` varchar(10) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Country Code',
  `full_textualday_format` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Full Textual Date Format',
  `call_center_logic` char(1) NOT NULL DEFAULT 'N',
  `notify_phone_lead_time` int(4) NOT NULL DEFAULT '20',
  `notify_phone_lag_time` int(4) NOT NULL DEFAULT '0',
  `base_url` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_client_deployment_config_client_id` (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `error`;
CREATE TABLE `error` (
  `id` int(11) NOT NULL,
  `error_code` varchar(100) NOT NULL,
  `error_message` varchar(200) DEFAULT NULL,
  `error_description` varchar(200) NOT NULL,
  `error_vxml` longtext,
  `send_alert` char(1) DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `resv_report_config`;
CREATE TABLE `resv_report_config` (
	`id` INT(4) NOT NULL AUTO_INCREMENT,
	`timestamp` DATETIME NOT NULL COMMENT 'set current timestamp',
	`username` VARCHAR(30) NOT NULL,
	`client_id` INT(4) NOT NULL,
	`report_name` VARCHAR(50) NOT NULL,
	`location_ids` VARCHAR(100) NULL DEFAULT NULL,
	`event_ids` VARCHAR(100) NULL DEFAULT NULL,
	`seat_ids` VARCHAR(100) NULL DEFAULT NULL,
	`procedure_ids` VARCHAR(100) NULL DEFAULT NULL,
	`department_ids` VARCHAR(100) NULL DEFAULT NULL,
	`report_columns` VARCHAR(1000) NULL,
	`resv_status_fetch` VARCHAR(20) NULL,
	`report_path` VARCHAR(200) NOT NULL,
	`email1` VARCHAR(60) NULL DEFAULT NULL,
	`email2` VARCHAR(60) NULL DEFAULT NULL,
	`email3` VARCHAR(60) NULL DEFAULT NULL,
	`email4` VARCHAR(60) NULL DEFAULT NULL,
	`email5` VARCHAR(60) NULL DEFAULT NULL,
	`email6` VARCHAR(60) NULL DEFAULT NULL,
	`sortby1` VARCHAR(50) NULL DEFAULT NULL,
	`sortby2` VARCHAR(50) NULL DEFAULT NULL,
	`sortby3` VARCHAR(50) NULL DEFAULT NULL,
	`sortby4` VARCHAR(50) NULL DEFAULT NULL,
	`sortby5` VARCHAR(50) NULL DEFAULT NULL,
	`report_stop` CHAR(1) NOT NULL DEFAULT 'N',
	`no_interval_hrs` INT(1) NULL DEFAULT '24',
	`report_no_days` INT(1) NULL DEFAULT '1',
	`file_format` VARCHAR(50) NULL DEFAULT 'PDF',
	`last_run_date` DATETIME NULL DEFAULT NULL,
	`enable` CHAR(1) NULL DEFAULT 'Y',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `login_attempts`;
CREATE TABLE `login_attempts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `user_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ip_address` varchar(50) NOT NULL,
  `login_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `outlook_login`;
CREATE TABLE `outlook_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `username` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `client_code` varchar(50) NOT NULL,
  `client_id` int(11) DEFAULT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

insert  into `client`(`id`,`timestamp`,`client_name`,`client_code`,`contact_person`,`contact_phone`,`contact_email`,`website`,`phone_transfer_number`,`fax`,`address`,`address2`,`city`,`state`,`zip`,`country`,`appt_link`,`forward_url`,`business_type`,`appcode`,`db_server`,`db_name`,`license_key`,`date_subscribed`,`date_cancelled`,`delete_flag`,`active`,`locked`,`group_account`,`channel_partner_id`,`sales_rep`,`billable`,`allow_online_appt`,`allow_ivr_appt`,`allow_mobile_appt`,`allow_mobile_admin`,`allow_ivr_notify`,`allow_sms_notify`,`allow_email_notify`,`cache_enabled`) values 
(1,'2015-02-19 00:00:00','Demo Resv','DEMORESV','Some User','123-123-1234','info@itfrontdesk.com','','','','123 Main St','','Nashville','TN','37201','USA','http://development.itfontdesk.com/demoresv','http://development.itfontdesk.com/admin','','resv','localhost:3306','resvtest','6.0.0','2012-01-20','2014-12-31','N','Y','N','','','','N','Y','Y','N','Y','Y','Y','Y','N');
INSERT INTO `admin_login_new` (`id`, `client_id`, `username`, `password`, `first_name`, `last_name`, `contact_phone`, `contact_email`, `location_ids`, `resource_ids`, `start_date`, `expire_date`, `suspend`, `access_level`, `password_last_update_date`, `password_reset_security_question_1`, `password_reset_security_answer_1`, `password_reset_security_question_2`, `password_reset_security_answer_2`, `password_reset_security_question_3`, `password_reset_security_answer_3`, `password_reset_algorithm`, `password_reset_sms_phone`, `wrong_login_max_attempt_locked`) VALUES (1, 1, 'demoresv', 'gDY1PUqbfd65FTEIMXiqeA==', 'Demo', 'User', NULL, 'info@itfrontdesk.com', '', '', '2014-06-01 00:00:00', '2020-01-01 10:10:10', 'N', 'Super User', '2014-06-01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO `client_deployment_config` (`id`, `client_id`, `block_time_in_mins`, `resource_calendar_months`, `user_expiry_date_time`, `day_start_time`, `day_end_time`, `integrated_reminddesk`, `server_flag`, `auto_file_upload`, `same_multiloc_resource`, `time_zone`, `date_format`, `date_yyyy_format`, `full_date_format`, `full_datetime_format`, `popup_calendardate_format`, `time_format`, `time_withsec_format`, `phone_format`, `country_code`, `full_textualday_format`, `call_center_logic`, `notify_phone_lead_time`, `notify_phone_lag_time`, `base_url`) VALUES (1, 1, 15, 18, '2020-12-31 00:00:00', '05:00:00', '20:00:00', 'Y', 'N', 'N', 'N', 'US/Central', 'MM/dd/yy', 'MM/dd/yyyy', 'MMMMM dd, yyyy', 'MMMMM dd, yyyy hhmma', 'MM/dd/yyyy', 'hh:mm a', 'hh:mm:ss a', 'US', 'US', 'EEEEE, MMM d, yyyy', 'Y', 0, 0, NULL);


insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1000','GET_CLIENT','Failed in getclientInfo method.','Failed in getclientInfo method.','','N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1001','GET_LOGIN_INFO','Failed in getLoginInfo','Failed in getLoginInfo',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1002','AUTH_CUSTOMER','Failed in authenticate customer method','Failed in authenticate customer method','<form id=\"%PAGE_NAME%\">\r\n    <block>\r\n        <audio src=\"file:///opt/telappoint/audio/ivrresv/laila/client/JCCEOCAA/us-en/wav/single_company.wav\">Your appointment is for</audio>\r\n        <audio src=\"file:///opt/telappoint/audio/ivrresv/laila/client/JCCEOCAA/us-en/wav/%ITEM_AUDIO%.wav\">%ITEM_TTS%</audio>\r\n        <submit\r\n            fetchaudio=\"file:///opt/telappoint/audio/ivrresv/laila/general/us-en/wav/fetchmusic.wav\"\r\n            fetchtimeout=\"120s\" method=\"get\" next=\"/ivrresv/getNextActionItem\"/>\r\n    </block>\r\n</form>','N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1003','GET_RESV_DETAILS_SEL_INFO','Failed in getResvDetailsSelectionInfo method','Failed in getResvDetailsSelectionInfo method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1004','GET_COMPANY_LIST','Failed in getCompanyList method','Failed in getCompanyList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1005','GET_PROCEDURE_LIST','Failed in getProcedureList method','Failed in getProcedureList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1006','GET_LOCATION_LIST','Failed in getLocationList method','Failed in getLocationList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1007','GET_DEPARTMENT_LIST','Failed in getDepartmentList method','Failed in getDepartmentList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1008','GET_EVENT_LIST','Failed in getEventList method','Failed in getEventList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1009','GET_EVENT_DATES_LIST','Failed in getEventDatesList method','Failed in getEventDatesList method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1010','GET_EVENT_TIMES','Failed in getEventTimes method','Failed in getEventTimes method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1011','GET_EVENT_SEATS','Failed in getEventSeats method','Failed in getEventSeats method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1012','HOLD_RESERVATION','Failed in holdreservation method','Failed in holdreservation method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1013','CONFIRM_RESERVATION','Failed in confirmreservation method','Failed in confirmreservation method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1014','CANCEL_RESERVATION','Failed in cancelreservation method','Failed in cancelreservation method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1015','HOLD_RELEASE_EVENT_TIME','Failed in holdreleas event time method','Failed in holdreleas event time method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1016','GET_EVENT_HISTORY','Failed in getEventHistory method','Failed in getEventHistory',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1017','GET_VERIFY_RESERVATION','Failed in getVerifyReservation method','Failed in getVerifyReservation method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1018','LIST_OF_THINGS_TO_BRING','Failed in listofthingstobring','Failed in listofthingstobring',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1019','UPDATE_RECORD_VXML','Failed in updateRecordVXML','Failed in updateRecordVXML',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1020','GET_NAME_RECORD_VXML','Failed in getRecordVXML','Failed in getRecordVXML',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1021','CONF_PAGE_CONTACTS_DETAILS','Failed in confpageconstantsdetails','Failed in confpageconstantsdetails',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1022','GET_ADMIN_LOCATION_LIST','Failed in getLocationList','Failed in getLocationList',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1023','DELETE_LOCATION','Failed in deleteLocation','Failed in deleteLocation',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1024','OPEN_CLOSE_LOCATION','Failed in openclose location','Failed in openclose location',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1025','GET_ADMIN_EVENT_LIST','Failed in getAdminEventList','Failed in getAdminEventList',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1026','DELETE_EVENT','Failed in deleteEvent','Failed in deleteEvent',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1027','OPEN_CLOSE_EVENT','Failed in openClose Event','Failed in openClose Event',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1028','ADD_LOCATION','Failed in AddLocation method','Failed in AddLocation method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1029','ADD_EVENT','Failed in AddEvent method','Failed in AddEvent method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1030','GET_ADMIN_EVENT_DATE_TIME_LIST','Failed in getEventDateTime method','Failed in getEventDateTime method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1031','OPEN_CLOSE_EVENT_DATE_TIME','Failed in openCloseEventDateTime','Failed in openCloseEventDateTime',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1032','ADD_EVENT_DATE_TIME','Failed in addEventDateTime','Failed in addEventDateTime',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1033','UPDATE_EVENT_DATE_TIME','Failed in updateEventDateTime','Failed in updateEventDateTime',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1034','LOGININFO_PAGE_RIGHT_SIDE_CONTENT','Failed in loginInfo page right side content','Failed in loginInfo page right side content',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1035','RESV_DETAILS_RIGHT_SIDE_CONTENT','Failed in resv details right side content','Failed in resv details right side content',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1036','RESV_VERIFY_PAGE_RIGHT_SIDE_CONTENT','Failed in resv verification right side content','Failed in resv verification right side content',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1037','CONFIRMATION_PAGE_RIGHT_SIDE_CONTENT','Failed in resv confirmation page right side info','Failed in resv confirmation page right side info',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1038','GET_RESERVED_EVENTS','Failed in GET_RESERVED_EVENTS','Failed in GET_RESERVED_EVENTS',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1039','SPECIFIC_DATE','Failed in specific_date','Failed in specific date method',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1040','GET_ADMIN_CALENDAR_OVERVIEW_LIST','Failed in getAdminCalendarOverList','Failed in getAdminCalendarOverList',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1041','DISCONNECT_VXML','Failed in disconnect vxml','Failed in disconnect vxml',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1042','SEATS_CALENDAR_VIEW','Failed in seats calendar view','Failed in seats calendar view',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1043','DAILY_CALENDAR_VIEW','Failed in dailyCalendar','Failed in dailyCalendar',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1044','GET_DATE_LIST','Failed in getDataList in dailycalendar view','Failed in getDataList in dailycalendar view',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1045','GET_HOME_PAGE_DETAILS','Failed in getHomePageDetails','Failed in getHomePageDetails',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1046','SEATS_CALENDAR_TIME_LIST','Failed in SEATS_CALENDAR_TIME_LIST','Failed in SEATS_CALENDAR_TIME_LIST',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1047','UPDATE_LOCATION','Failed in Update  location','Failed in Update location',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1048','UPDATE_EVENT','Failed in update Event','Failed in update Event',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1049','GET_CUSTOMER_NAMES','Get Customer Names','Get Customer Names',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1050','RESERVATION_REPORTS','get reservation reports ','get reservation reports ',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1051','UPDATE_EVENT_SEATS','Failed in update seats','Failed in update seats',NULL,'N');

-- 18th aug 2015
-- balaji
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1052','CALENDAR_OVERVIEW_DETAILS','Failed in calendar overview details API','Failed in calendar overview details API',NULL,'N');


insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1053','FUTURE_RESERVATION_DETAILS','Failed in future resvs API','Failed in future resvs API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1054','PAST_RESERVATION_DETAILS','Failed in past resvs API','Failed in past resvs API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1055','GET_CUSTOMER_ID','Failed in getCustomerById API','Failed in getCustomerById API',NULL,'N');

-- 23rd aug 2015
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1056','RESERVATION_DETAILS','Failed in getReservationDetails','Failed in getReservationDetails API',NULL,'N');

insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1057','GET_REGISTRATION_INFO','Failed in GET_REGISTRATION_INFO','Failed in GET_REGISTRATION_INFO API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1058','RESERVATION_SEARCH','Failed in RESERVATION_SEARCH','Failed in RESERVATION_SEARCH API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1059','UPDATE_CUSTOMER','Failed in CREATE_OR_UPDATE_CUSTOMER','Failed in CREATE_OR_UPDATE_CUSTOMER API',NULL,'N');

insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1060','TABLE_PRINT_VIEW','Failed in TABLE_PRINT_VIEW','Failed in TABLE_PRINT_VIEW API',NULL,'N');


insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1061','DYNAMIC_SEARCH_FIELDS','Failed in DYNAMIC_SEARCH_FIELDS','Failed in DYNAMIC_SEARCH_FIELDS API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1062','CLIENT_DETAILS','Failed in CLIENT_DETAILS','Failed in CLIENT_DETAILS API',NULL,'N');

-- 8th Sep
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1063','GRAPH_DETAILS','Failed in GRAPH_DETAILS','Failed in GRAPH_DETAILS API',NULL,'N');

-- 14th sep
update `error`  set error_code='UPDATE_CUSTOMER',error_message='Failed in UPDATE_CUSTOMER', error_description='Failed in UPDATE_CUSTOMER'  where  error_code='CREATE_OR_UPDATE_CUSTOMER';

-- 16th sep
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1064','PRIVILAGE_DETAILS','Failed in PRIVILAGE_DETAILS','Failed in PRIVILAGE_DETAILS API',NULL,'N');

-- 19th sep
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1065','GET_LOCATION_BY_ID','Failed in GET_LOCATION_BY_ID','Failed in GET_LOCATION_BY_ID API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1066','GET_EVENT_BY_ID','Failed in GET_EVENT_BY_ID','Failed in GET_EVENT_BY_ID API',NULL,'N');

-- 20TH SEP
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1067','BLOCKED_CLIENT_DETAILS','Failed in BLOCKED_CLIENT_DETAILS','Failed in BLOCKED_CLIENT_DETAILS API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1068','GET_CAMPAIGN_BY_ID','Failed in GET_CAMPAIGN_BY_ID','Failed in GET_CAMPAIGN_BY_ID API',NULL,'N');


-- 24th Sep
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1069','ADD_RESV_REPORT_CONFIG','Failed in ADD_RESV_REPORT_CONFIG','Failed in ADD_RESV_REPORT_CONFIG API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1070','GET_RESV_REPORT_CONFIG','Failed in GET_RESV_REPORT_CONFIG','Failed in GET_RESV_REPORT_CONFIG API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1071','DELETE_REPORT_CONFIG_BY_ID','Failed in DELETE_REPORT_CONFIG_BY_ID','Failed in DELETE_REPORT_CONFIG_BY_ID API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1072','RESERVATION_REMINDERS','Failed in RESERVATION_REMINDERS','Failed in RESERVATION_REMINDERS API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1073','GET_CAMPAIGNS','Failed in GET_CAMPAIGNS','Failed in GET_CAMPAIGNS API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1074','GET_RESERVATION_STATUS_LIST','Failed in GET_RESERVATION_STATUS_LIST','Failed in GET_RESERVATION_STATUS_LIST API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1075','GET_REMINDER_STATUS_LIST','Failed in GET_REMINDER_STATUS_LIST','Failed in GET_REMINDER_STATUS_LIST API',NULL,'N');
-- 6th oct
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1076','UPDATE_REMINDER_STATUS','Failed in UPDATE_REMINDER_STATUS','Failed in UPDATE_REMINDER_STATUS API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1077','CHANGE_SCHEDULER_STATUS','Failed in CHANGE_SCHEDULER_STATUS','Failed in CHANGE_SCHEDULER_STATUS API',NULL,'N');

insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1078','GET_NOTIFICATION_LIST','Failed in GET_NOTIFICATION_LIST','Failed in GET_NOTIFICATION_LIST API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1079','LOCK_DIALER_LOCK','Failed in LOCK_DIALER_LOCK','Failed in LOCK_DIALER_LOCK API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1080','UNLOCK_DIALER_LOCK','Failed in UNLOCK_DIALER_LOCK','Failed in UNLOCK_DIALER_LOCK API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1081','GET_DIALER_SETTING','Failed in GET_DIALER_SETTING','Failed in GET_DIALER_SETTING API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1082','GET_NOTIFY_LIST','Failed in GET_NOTIFY_LIST','Failed in GET_NOTIFY_LIST API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1083','CHECK_ALLOW_DUPLICATE_RESV','Failed in CHECK_ALLOW_DUPLICATE_RESV','Failed in CHECK_ALLOW_DUPLICATE_RESV API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1084','GET_INBOUND_CALL_REPORT_LIST','Failed in GET_INBOUND_CALL_REPORT_LIST','Failed in GET_INBOUND_CALL_REPORT_LIST API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1085','GET_OUTBOUND_CALL_REPORT_LIST','Failed in GET_OUTBOUND_CALL_REPORT_LIST','Failed in GET_OUTBOUND_CALL_REPORT_LIST API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1086','GET_TRANS_STATS_LIST','Failed in GET_TRANS_STATS_LIST','Failed in GET_TRANS_STATS_LIST API',NULL,'N');

commit;


