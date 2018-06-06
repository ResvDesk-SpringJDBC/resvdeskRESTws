/*
SQLyog Community Edition- MySQL GUI v5.27
Host - 5.5.5-10.0.20-MariaDB : Database - demoresv2
*********************************************************************
Server version : 5.5.5-10.0.20-MariaDB
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `demoresv_new`;

USE `demoresv_new`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `access_privilege` */

DROP TABLE IF EXISTS `access_privilege`;

CREATE TABLE `access_privilege` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `privilege` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `campaign` */

DROP TABLE IF EXISTS `campaign`;

CREATE TABLE `campaign` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(90) NOT NULL,
  `notify_by_phone` char(1) DEFAULT NULL,
  `notify_by_phone_confirm` char(1) DEFAULT NULL,
  `notify_by_sms` char(1) DEFAULT NULL,
  `notify_by_sms_confirm` char(1) DEFAULT NULL,
  `notify_by_email` char(1) DEFAULT NULL,
  `notify_by_email_confirm` char(1) DEFAULT NULL,
  `notify_by_push_notification` char(1) DEFAULT NULL,
  `active` char(1) DEFAULT 'Y',
  `delete_flag` char(1) DEFAULT 'N',
  `placement` tinyint(4) DEFAULT '1' COMMENT 'Order Placement',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `campaign_email_response_action` */

DROP TABLE IF EXISTS `campaign_email_response_action`;

CREATE TABLE `campaign_email_response_action` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `action_page` varchar(50) NOT NULL,
  `page_header` varchar(1000) NOT NULL,
  `page_content` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `campaign_email_response_lookup_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `campaign_message_email` */

DROP TABLE IF EXISTS `campaign_message_email`;

CREATE TABLE `campaign_message_email` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `lang` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'us-en',
  `subject` varchar(200) DEFAULT NULL,
  `message` text,
  `last_update_username` varchar(90) NOT NULL,
  `last_update_timestamp` datetime NOT NULL,
  `enable_html_flag` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cm_em` (`campaign_id`,`lang`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `campaign_message_email_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `campaign_message_phone` */

DROP TABLE IF EXISTS `campaign_message_phone`;

CREATE TABLE `campaign_message_phone` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `lang` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'us-en',
  `tts_only` char(1) NOT NULL,
  `message` text,
  `last_update_username` varchar(90) NOT NULL,
  `last_update_timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cm_phone` (`campaign_id`,`lang`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `campaign_message_phone_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `campaign_message_sms` */

DROP TABLE IF EXISTS `campaign_message_sms`;

CREATE TABLE `campaign_message_sms` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `lang` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'us-en',
  `subject` varchar(200) DEFAULT NULL,
  `message` text,
  `input_value_1` varchar(200) DEFAULT NULL,
  `input_response_1` varchar(2000) DEFAULT NULL,
  `input_value_2` varchar(200) DEFAULT NULL,
  `input_response_2` varchar(2000) DEFAULT NULL,
  `input_value_3` varchar(200) DEFAULT NULL,
  `input_response_3` varchar(2000) DEFAULT NULL,
  `input_value_4` varchar(200) DEFAULT NULL,
  `input_response_4` varchar(2000) DEFAULT NULL,
  `input_value_5` varchar(200) DEFAULT NULL,
  `input_response_5` varchar(2000) DEFAULT NULL,
  `else_response` varchar(2000) DEFAULT NULL,
  `last_update_username` varchar(90) NOT NULL,
  `last_update_timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cm_sms` (`campaign_id`,`lang`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `campaign_message_sms_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `campaign_sms_response_action` */

DROP TABLE IF EXISTS `campaign_sms_response_action`;

CREATE TABLE `campaign_sms_response_action` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `input_value` varchar(5) NOT NULL,
  `action` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `campaign_sms_response_lookup_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `company_name_online` varchar(100) NOT NULL DEFAULT '',
  `company_name_mobile` varchar(100) NOT NULL DEFAULT '',
  `company_name_sms` varchar(100) NOT NULL DEFAULT '',
  `company_name_ivr_tts` varchar(100) NOT NULL DEFAULT '',
  `company_name_ivr_audio` varchar(100) NOT NULL DEFAULT '',
  `company_name_remind_sms` varchar(100) DEFAULT NULL,
  `delete_flag` char(1) DEFAULT NULL,
  `placement` int(6) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `company_procedure` */

DROP TABLE IF EXISTS `company_procedure`;

CREATE TABLE `company_procedure` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `company_id` int(10) NOT NULL,
  `procedure_id` int(10) NOT NULL,
  `enable` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`),
  KEY `FK_company_procedure_company` (`company_id`),
  KEY `FK_company_procedure_procedure` (`procedure_id`),
  CONSTRAINT `FK_company_procedure_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FK_company_procedure_procedure` FOREIGN KEY (`procedure_id`) REFERENCES `procedure` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each customer',
  `account_number` varchar(30) NOT NULL COMMENT 'Account number of the customer provided by the Client',
  `first_name` varchar(30) NOT NULL COMMENT 'First name of the Customer',
  `middle_name` varchar(30) DEFAULT NULL COMMENT 'Middle name of the Customer',
  `last_name` varchar(30) NOT NULL COMMENT 'Last name of the Customer',
  `prev_first_name` varchar(30) NOT NULL DEFAULT '',
  `prev_last_name` varchar(30) NOT NULL DEFAULT '',
  `transcribed` char(1) NOT NULL DEFAULT 'N',
  `contact_phone` varchar(15) DEFAULT NULL,
  `home_phone` varchar(15) DEFAULT NULL,
  `work_phone` varchar(15) DEFAULT NULL,
  `cell_phone` varchar(15) DEFAULT NULL,
  `dob` date DEFAULT NULL COMMENT 'Date of birth of the Customer',
  `sex` varchar(10) DEFAULT NULL COMMENT 'Sex of the Customer',
  `pin` varchar(8) DEFAULT '1234' COMMENT 'Pin code set by the Customer for additional security',
  `email` varchar(60) DEFAULT NULL COMMENT 'Email address of the Customer',
  `remind_lang` varchar(6) DEFAULT NULL COMMENT 'Language Code for the Reminder Voice Call',
  `remind_phone` varchar(10) DEFAULT NULL COMMENT 'Reminder Phone for the Customer',
  `remind_time` tinyint(5) DEFAULT '0' COMMENT 'Reminder Time Range Code. 1 = 8am - 12 pm; 2 = 12pm - 5pm; 3 = 5pm - 8pm. or it could be combination of 12, 123 etc',
  `provider_ids` varchar(500) DEFAULT NULL COMMENT 'Comma separated Provider Ids associated with the Customer',
  `location_ids` varchar(500) DEFAULT NULL COMMENT 'Comma separated Location Ids associated with the Customer',
  `address` varchar(200) DEFAULT NULL COMMENT 'Address of the Customer',
  `address_2` varchar(200) DEFAULT NULL COMMENT 'Address of the Customer continued',
  `city` varchar(50) DEFAULT NULL COMMENT 'City of Customer',
  `state` varchar(80) DEFAULT NULL COMMENT 'State Code of the Customer',
  `zip_postal` varchar(25) DEFAULT NULL COMMENT 'Zip/Postal code of the Customer',
  `country` varchar(100) DEFAULT NULL COMMENT 'Country of the Customer',
  `attrib1` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 1 defined in  customer_attrib_config table',
  `attrib2` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 2 defined in  customer_attrib_config table',
  `attrib3` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 3 defined in  customer_attrib_config table',
  `attrib4` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 4 defined in  customer_attrib_config table',
  `attrib5` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 5 defined in  customer_attrib_config table',
  `attrib6` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 6 defined in  customer_attrib_config table',
  `attrib7` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 7 defined in  customer_attrib_config table',
  `attrib8` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 8 defined in  customer_attrib_config table',
  `attrib9` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 9 defined in  customer_attrib_config table',
  `attrib10` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 10 defined in  customer_attrib_config table',
  `attrib11` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 11 defined in  customer_attrib_config table',
  `attrib12` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 12 defined in  customer_attrib_config table',
  `attrib13` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 13 defined in  customer_attrib_config table',
  `attrib14` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 14 defined in  customer_attrib_config table',
  `attrib15` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 15 defined in  customer_attrib_config table',
  `attrib16` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 16 defined in  customer_attrib_config table',
  `attrib17` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 17 defined in  customer_attrib_config table',
  `attrib18` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 18 defined in  customer_attrib_config table',
  `attrib19` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 19 defined in  customer_attrib_config table',
  `attrib20` varchar(200) DEFAULT NULL COMMENT 'Generic Attribute 20 defined in  customer_attrib_config table',
  `create_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Customer record creation date and time',
  `update_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Customer record latest update date and time',
  `delete_flag` char(1) CHARACTER SET latin1 DEFAULT 'N' COMMENT 'Y = Soft deleted Customer, N =  Valid Customer',
  `blocked_flag` char(1) CHARACTER SET latin1 DEFAULT 'N' COMMENT 'Y = Making appt is Blocked for Customer, N =  Customer can make appts.',
  `email_bcc` varchar(255) DEFAULT 'N',
  `email_cc` varchar(255) DEFAULT 'N',
  `notify_by_email` varchar(1) DEFAULT 'N',
  `notify_by_email_confirm` varchar(1) DEFAULT 'N',
  `notify_by_push` varchar(1) DEFAULT 'N',
  `notify_by_phone` varchar(1) DEFAULT 'N',
  `notify_by_phone_confirm` varchar(1) DEFAULT 'N',
  `notify_by_sms` varchar(1) DEFAULT 'N',
  `notify_by_sms_confirm` varchar(1) DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `index_fn` (`first_name`),
  KEY `index_ln` (`last_name`),
  KEY `index_accno` (`account_number`),
  KEY `index_hphone` (`home_phone`),
  KEY `index_blocked` (`blocked_flag`),
  KEY `index_delete` (`delete_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains Customer data';

/*Table structure for table `customer_attrib_config` */

DROP TABLE IF EXISTS `customer_attrib_config`;

CREATE TABLE `customer_attrib_config` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each row',
  `attrib_column` varchar(50) NOT NULL COMMENT 'Column name of the Customer Attribute like attrib1, attrib2....  , attrib20',
  `attrib_name` varchar(200) NOT NULL COMMENT 'Client Specific Attribute Name',
  `attrib_type` varchar(50) DEFAULT NULL COMMENT 'Data type of Attribute like int , string ',
  `attrib_max_chars` smallint(6) NOT NULL COMMENT 'Specify max characters if Login field is String type',
  `attrib_min_value` smallint(6) NOT NULL COMMENT 'Specify min value of Login field here if Login field is Integer type',
  `attrib_max_value` smallint(6) NOT NULL COMMENT 'Specify max value of Login field here if Login field is Integer type',
  `list_labels` varchar(500) NOT NULL COMMENT 'CSV list of labels if Login field is List, Radio, Checkbox, Multibox etc',
  `list_values` varchar(500) NOT NULL COMMENT 'CSV list of values if Login field is List, Radio, Checkbox, Multibox etc',
  `list_initial_values` varchar(500) NOT NULL COMMENT 'CSV list of initial values if Login field is List, Radio, Checkbox, Multibox etc',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table defines Customizable Attributes 1 to 20 in Custom';

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID - dept_id - autoincrement',
  `department_name_online` varchar(100) NOT NULL DEFAULT '' COMMENT 'Deaprtment Name for Online display',
  `department_name_mobile` varchar(100) NOT NULL DEFAULT '' COMMENT 'Department Name for Mobile Apps - normally shorter name to fit the screen',
  `department_name_sms` varchar(100) NOT NULL DEFAULT '' COMMENT 'Department Name for SMS - abbreviated text',
  `department_name_ivr_tts` varchar(100) NOT NULL COMMENT 'Department Name for IVR TTS, which could be different from actual display',
  `department_name_ivr_audio` varchar(100) NOT NULL DEFAULT '' COMMENT 'Department Name for IVR audio file name',
  `department_name_remind_sms` varchar(20) NOT NULL DEFAULT '' COMMENT 'Department Name for SMS reminder text',
  `delete_flag` char(1) NOT NULL DEFAULT 'N' COMMENT 'soft delete flag. Y = delete; N = active',
  `placement` int(6) DEFAULT '1' COMMENT 'order of placement on the dropdown list',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `dialer_lock` */

DROP TABLE IF EXISTS `dialer_lock`;

CREATE TABLE `dialer_lock` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `locked` char(1) DEFAULT 'N',
  `device_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `devtype` (`device_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `dialer_settings` */

DROP TABLE IF EXISTS `dialer_settings`;

CREATE TABLE `dialer_settings` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `last_update_username` varchar(90) NOT NULL,
  `last_update_timestamp` datetime NOT NULL,
  `campaign_id` bigint(20) unsigned NOT NULL COMMENT 'Remind Campaign Id linked to remind_campaign table',
  `no_phone_lines` int(4) NOT NULL COMMENT 'Number of Phone Lines for the Dialer',
  `mins_before_start_calling` int(4) DEFAULT NULL COMMENT 'How many minutes before start calling',
  `days_before_start_calling` int(4) DEFAULT NULL COMMENT 'How many days before start calling',
  `bus_days_before_start_calling` int(4) DEFAULT NULL COMMENT 'How many business days before start calling',
  `mins_stop_calling` int(4) DEFAULT NULL COMMENT 'How many minutes before stop calling',
  `hours_stop_calling` int(4) DEFAULT NULL COMMENT 'How many hours before stop calling',
  `tot_max_attempts` int(4) DEFAULT NULL COMMENT 'Total number of Attempts for all days',
  `max_attempts_per_day` int(4) DEFAULT NULL COMMENT 'Total number of Attempts per day',
  `time_interval_between_attempts` int(4) NOT NULL COMMENT 'Time Interval between Attempts',
  `call_first` int(4) NOT NULL COMMENT 'Dial which phone number first? 0 = Do not Call, 1 = Home Phone, 2 = Work Phone, 3 = Cell Phone, 4 = SMS',
  `call_second` int(4) DEFAULT NULL COMMENT 'Dial which phone number second? 0 = Do not Call, 1 = Home Phone, 2 = Work Phone, 3 = Cell Phone, 4 = SMS',
  `call_third` int(4) DEFAULT NULL COMMENT 'Dial which phone number third? 0 = Do not Call, 1 = Home Phone, 2 = Work Phone, 3 = Cell Phone, 4 = SMS',
  `call_fourth` int(4) DEFAULT NULL COMMENT 'Dial which phone number fourth? 0 = Do not Call, 1 = Home Phone, 2 = Work Phone, 3 = Cell Phone, 4 = SMS',
  `dial_all_phones` char(1) DEFAULT NULL COMMENT 'Diall All Phone Numbers even if first dial was successful? Y = yes. N = no.',
  `leave_msg` char(1) NOT NULL COMMENT 'Leave Message in Answering Machine. Y = yes. N = no.',
  `msg_after_no_attempt` int(4) DEFAULT NULL COMMENT 'If above yes, after how many attempts? 0 = first attempt',
  `from_address` varchar(90) DEFAULT NULL COMMENT 'Email From Address',
  `replyto_address` varchar(90) DEFAULT NULL COMMENT 'Email ReplyTo Address',
  `dns_mail_host` varchar(90) DEFAULT NULL COMMENT 'Email DNS Hostname',
  `reply_sms_forward_email` varchar(90) DEFAULT NULL COMMENT 'Email Address to forward the SMS Reply',
  `call_forward_phone` varchar(10) DEFAULT NULL COMMENT 'Call Transfer Number for Live Agent',
  `caller_id` varchar(10) DEFAULT NULL COMMENT 'Caller ID Display on the reminder phone call',
  `sms_id` varchar(10) DEFAULT NULL,
  `caller_name` varchar(20) DEFAULT NULL COMMENT 'Caller Name Display on the reminder phone call',
  `call_sun` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Sunday? Y = yes, N = no',
  `call_mon` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Monday? Y = yes, N = no',
  `call_tue` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Tuesday? Y = yes, N = no',
  `call_wed` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Wednesday? Y = yes, N = no',
  `call_thu` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Thursday? Y = yes, N = no',
  `call_fri` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Friday? Y = yes, N = no',
  `call_sat` char(1) NOT NULL DEFAULT '' COMMENT 'Can remind on Saturday? Y = yes, N = no',
  `call_from_1` time NOT NULL COMMENT 'Call Start Time - first block',
  `call_to_1` time NOT NULL COMMENT 'Call End Time - first block',
  `call_from_2` time DEFAULT NULL COMMENT 'Call Start Time - second block',
  `call_to_2` time DEFAULT NULL COMMENT 'Call End Time - second block',
  `area_code` varchar(3) DEFAULT NULL COMMENT 'Default Area Code to prefix if only 7 digit phone number',
  `alert_phone` varchar(10) DEFAULT NULL COMMENT 'Alert Phone if file upload is missing',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dail_setu` (`campaign_id`),
  KEY `campaign_id` (`campaign_id`),
  CONSTRAINT `dialer_settings_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `display_names` */

DROP TABLE IF EXISTS `display_names`;

CREATE TABLE `display_names` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID - auto_increment',
  `device` varchar(20) DEFAULT NULL,
  `company_name` varchar(100) NOT NULL DEFAULT 'Company',
  `companies_name` varchar(100) NOT NULL DEFAULT 'Company',
  `company_select` varchar(100) NOT NULL DEFAULT 'Select Company',
  `company_audio` varchar(100) DEFAULT NULL,
  `procedure_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'procedure display name. example: Procedure, Appt Type, Ward Number etc',
  `procedures_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'procedure name in plural',
  `procedure_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'procedure name in select dropdowns',
  `procedure_audio` varchar(100) DEFAULT NULL COMMENT 'audio file name for procedure',
  `location_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'location display name. example: Location, Degree Programs, Ward Number etc',
  `locations_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'location name in plural',
  `location_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'location name in select dropdowns',
  `location_audio` varchar(100) DEFAULT NULL COMMENT 'audio file name for location',
  `department_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'department display name. example: Department, School Type, Degree Programs, etc',
  `departments_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'department name in plural',
  `department_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'department name in select dropdowns',
  `department_audio` varchar(100) DEFAULT NULL COMMENT 'audio file name for department',
  `event_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'resource display name. example: Doctor, Counselor, Advisor, Intake, Professional, Hair Stylist, etc',
  `events_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'resource name in plural',
  `event_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'resource name in select dropdowns',
  `event_audio` varchar(100) DEFAULT NULL COMMENT 'audio file name for resource',
  `seat_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'service display name. example: Service, No of Person, Course Name, etc',
  `seats_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'service name in plural',
  `seat_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'service name in select dropdowns',
  `seat_audio` varchar(100) DEFAULT NULL COMMENT 'audio file name for service',
  `customer_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'customer display name. example: Customer, Student, Patient, etc',
  `customers_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'customer name in plural',
  `customer_select` varchar(100) NOT NULL DEFAULT '' COMMENT 'customer name in select dropdowns',
  `customer_audio` varchar(100) NOT NULL DEFAULT '' COMMENT 'audio file name for customer',
  `comments_name` varchar(100) NOT NULL DEFAULT 'Comments',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `donot_notify` */

DROP TABLE IF EXISTS `donot_notify`;

CREATE TABLE `donot_notify` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `first_name` varchar(15) NOT NULL,
  `middle_name` varchar(15) DEFAULT NULL,
  `last_name` varchar(15) NOT NULL,
  `phone1` varchar(10) DEFAULT NULL,
  `phone2` varchar(10) DEFAULT NULL,
  `phone3` varchar(10) DEFAULT NULL,
  `home_phone` varchar(10) DEFAULT NULL,
  `work_phone` varchar(10) DEFAULT NULL,
  `cell_phone` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `push_notify_id` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `dynamic_search` */

DROP TABLE IF EXISTS `dynamic_search`;

CREATE TABLE `dynamic_search` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `category` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `table_column` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `java_reflection` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(1500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `java_type` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `field_type` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `search_type` char(1) COLLATE utf8_unicode_ci DEFAULT 'P' COMMENT 'P-Partial search, F-Full search',
  `select_alias` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `placement` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `dynamic_sql` */

DROP TABLE IF EXISTS `dynamic_sql`;

CREATE TABLE `dynamic_sql` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `category` varchar(200) NOT NULL,
  `sql_query` text,
  `dynamic_key_mapping` text,
  `enable` char(1) NOT NULL DEFAULT 'Y',
  `keys_comments` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `dynamic_table_items` */

DROP TABLE IF EXISTS `dynamic_table_items`;

CREATE TABLE `dynamic_table_items` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `category` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `table_name` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
  `table_column` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
  `alias_name` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
  `java_reflection` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(1500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hide` char(1) COLLATE utf8_unicode_ci DEFAULT 'N',
  `type` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `placement` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `email_sms_config` */

DROP TABLE IF EXISTS `email_sms_config`;

CREATE TABLE `email_sms_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `campaign_id` bigint(20) unsigned NOT NULL,
  `lang_id` int(10) NOT NULL,
  `type` varchar(200) DEFAULT NULL,
  `subject` varchar(2000) DEFAULT NULL,
  `body` text,
  `enable_html_flag` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`),
  UNIQUE KEY `em_sms_config` (`campaign_id`,`lang_id`),
  KEY `campaign_id` (`campaign_id`),
  KEY `lang_id` (`lang_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `event` */

DROP TABLE IF EXISTS `event`;

CREATE TABLE `event` (
  `event_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(100) NOT NULL,
  `event_name_ivr_tts` varchar(200) DEFAULT NULL,
  `event_name_ivr_audio` varchar(200) DEFAULT NULL,
  `allow_duplicates` char(1) default 'N',
  `placement` tinyint(4) DEFAULT NULL,
  `enable` char(1) DEFAULT 'Y',
  `delete_flag` char(1) NOT NULL DEFAULT 'N',
  `duration` int(6) NOT NULL,
  `send_reminder` char(1) DEFAULT 'Y',
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `event_date_time` */

DROP TABLE IF EXISTS `event_date_time`;

CREATE TABLE `event_date_time` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) unsigned NOT NULL,
  `location_id` int(10) unsigned NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `enable` char(1) COLLATE utf8_unicode_ci DEFAULT 'Y',
  `num_seats` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_id` (`event_id`,`location_id`,`date`,`time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `holidays` */

DROP TABLE IF EXISTS `holidays`;

CREATE TABLE `holidays` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `date` date NOT NULL COMMENT 'holiday date',
  `name` varchar(50) DEFAULT NULL COMMENT 'name of the holiday',
  PRIMARY KEY (`id`),
  UNIQUE KEY `date` (`date`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `i18n_aliases` */

DROP TABLE IF EXISTS `i18n_aliases`;

CREATE TABLE `i18n_aliases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lang` (`device`,`lang`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_design_templates` */

DROP TABLE IF EXISTS `i18n_design_templates`;

CREATE TABLE `i18n_design_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `device` (`device`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_display_button_names` */

DROP TABLE IF EXISTS `i18n_display_button_names`;

CREATE TABLE `i18n_display_button_names` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lang` (`device`,`lang`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_display_field_labels` */

DROP TABLE IF EXISTS `i18n_display_field_labels`;

CREATE TABLE `i18n_display_field_labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lang` (`device`,`lang`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_display_page_content` */

DROP TABLE IF EXISTS `i18n_display_page_content`;

CREATE TABLE `i18n_display_page_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `lang` (`device`,`lang`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_email_templates` */

DROP TABLE IF EXISTS `i18n_email_templates`;

CREATE TABLE `i18n_email_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lang` (`lang`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `i18n_lang` */

DROP TABLE IF EXISTS `i18n_lang`;

CREATE TABLE `i18n_lang` (
  `id` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Unique ID - auto_increment',
  `lang_code` varchar(5) DEFAULT NULL COMMENT 'Language Code. example: us-en for US English, us-es for US Spanish etc',
  `ivr_dtmf_input` tinyint(4) DEFAULT NULL COMMENT 'DTMF input to select this language. 0 for default language.',
  `ivr_voice_input` varchar(20) DEFAULT NULL COMMENT 'ASR (voice) input to select this language. 0 for default language',
  `tts_lang` varchar(10) DEFAULT NULL COMMENT 'Language code for TTS. example: en-US for US English. es-US for Spanish English',
  `asr_lang` varchar(10) DEFAULT NULL COMMENT 'Language code for ASR. example: en-US for US English. es-US for Spanish English',
  `language` varchar(50) DEFAULT NULL COMMENT 'Language Name',
  `lang_link` varchar(50) DEFAULT NULL COMMENT 'Language Link displayed on the device',
  `default_lang` char(1) NOT NULL COMMENT 'Default Language. Y = default. N = not a default language',
  `placement` int(4) NOT NULL COMMENT 'Placement order',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `i18n_sms_templates` */

DROP TABLE IF EXISTS `i18n_sms_templates`;

CREATE TABLE `i18n_sms_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_value` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lang` (`device`,`lang`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `ivr_audio` */

DROP TABLE IF EXISTS `ivr_audio`;

CREATE TABLE `ivr_audio` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id',
  `placement` int(11) NOT NULL,
  `form_id` bigint(20) unsigned NOT NULL COMMENT 'Form Id linked to form table',
  `field_id` bigint(20) unsigned NOT NULL COMMENT 'Field Id linked to field table',
  `block_id` bigint(20) unsigned DEFAULT NULL COMMENT 'Block Id linked to block table',
  `prompt_id` bigint(20) unsigned NOT NULL COMMENT 'Prompt Id linked to prompt table',
  `filled_id` bigint(20) unsigned DEFAULT NULL COMMENT 'Filled Id linked to filled table',
  `lang_id` bigint(20) unsigned DEFAULT '1',
  `path` varchar(2000) DEFAULT NULL,
  `src` varchar(2000) DEFAULT NULL COMMENT 'SRC value for the audio',
  `tts` varchar(2000) DEFAULT NULL COMMENT 'TTS value for the audio',
  `delete_flag` char(1) DEFAULT 'N',
  `file_type` varchar(20) DEFAULT '.wav',
  PRIMARY KEY (`id`),
  KEY `form_id` (`form_id`),
  KEY `field_id` (`field_id`),
  KEY `block_id` (`block_id`),
  KEY `prompt_id` (`prompt_id`),
  KEY `filled_id` (`filled_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_block` */

DROP TABLE IF EXISTS `ivr_block`;

CREATE TABLE `ivr_block` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id',
  `form_id` bigint(20) unsigned DEFAULT NULL,
  `field_id` bigint(20) unsigned DEFAULT NULL COMMENT 'Field Id linked to field table',
  PRIMARY KEY (`id`),
  KEY `field_id` (`field_id`),
  KEY `form_id` (`form_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_calls` */

DROP TABLE IF EXISTS `ivr_calls`;

CREATE TABLE `ivr_calls` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  `customer_id` bigint(20) unsigned DEFAULT NULL,
  `event_id` tinyint(4) DEFAULT NULL,
  `location_id` tinyint(4) DEFAULT NULL,
  `seat_id` tinyint(4) DEFAULT NULL,
  `conf_num` bigint(20) unsigned DEFAULT NULL,
  `appt_type` tinyint(4) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `seconds` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trans_id` (`trans_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ivr_vxml` */

DROP TABLE IF EXISTS `ivr_vxml`;

CREATE TABLE `ivr_vxml` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `page_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `audio` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tts` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang_code` varchar(5) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'us-en',
  `vxml` longtext COLLATE utf8_unicode_ci,
  `enabled` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `ivr_welcome` */

DROP TABLE IF EXISTS `ivr_welcome`;

CREATE TABLE `ivr_welcome` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id',
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'Welcome',
  `welcome_vxml` longtext COLLATE utf8_unicode_ci,
  `enabled` char(1) COLLATE utf8_unicode_ci DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `last_update_notify_record` */

DROP TABLE IF EXISTS `last_update_notify_record`;

CREATE TABLE `last_update_notify_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` datetime NOT NULL,
  `notify_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `last_update_notify_record_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `list_of_things_bring` */

DROP TABLE IF EXISTS `list_of_things_bring`;

CREATE TABLE `list_of_things_bring` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `device` varchar(20) NOT NULL DEFAULT '' COMMENT 'device name. values: online, sms, ivr, mobile, admin',
  `lang` varchar(6) DEFAULT NULL COMMENT 'Language Code',
  `event_id` int(10) NOT NULL COMMENT 'service id linked to service table',
  `display_text` text COMMENT 'list of things to bring display text',
  PRIMARY KEY (`id`),
  KEY `service_id` (`event_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `location_name` varchar(100) NOT NULL DEFAULT '' COMMENT 'Location name for online app',
  `location_name_ivr_tts` varchar(100) NOT NULL DEFAULT '' COMMENT 'Location name for ivr tts play',
  `location_name_ivr_audio` varchar(100) NOT NULL DEFAULT '' COMMENT 'Location filename for ivr audio play',
  `location_name_remind_sms` varchar(20) NOT NULL DEFAULT '' COMMENT 'Location name for sms reminder',
  `address` varchar(60) NOT NULL DEFAULT '' COMMENT 'Location Address',
  `city` varchar(30) NOT NULL DEFAULT '' COMMENT 'Location City',
  `state` varchar(2) NOT NULL DEFAULT '' COMMENT 'Location State',
  `zip` varchar(9) NOT NULL DEFAULT '' COMMENT 'Location Zip',
  `work_phone` varchar(15) NOT NULL DEFAULT '' COMMENT 'Location Phone',
  `contact_details` varchar(2000) NULL,
  `location_google_map` text COMMENT 'Location Google map',
  `location_google_map_link` text COMMENT 'Location Google map link',
  `time_zone` varchar(20) NOT NULL DEFAULT '' COMMENT 'Location Time Zone',
  `comments` varchar(100) DEFAULT NULL COMMENT 'Comments (could be used to store Catapult''s Location ID)',
  `delete_flag` char(1) NOT NULL DEFAULT 'N' COMMENT 'soft delete flag. Y = delete. N = active',
  `placement` int(6) DEFAULT '1' COMMENT 'Placement Order',
  `enable` char(1) NOT NULL DEFAULT 'Y',
  `closed` char(1) NOT NULL DEFAULT 'N',
  `closed_message` varchar(500) NOT NULL DEFAULT 'This Location is Currently Closed for Appointments',
  `closed_audio` varchar(100) NOT NULL DEFAULT 'location_closed',
  `closed_tts` varchar(500) NOT NULL DEFAULT 'This Location is Currently Closed for Appointments. Please check back later.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `location_department_event` */

DROP TABLE IF EXISTS `location_department_event`;

CREATE TABLE `location_department_event` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `location_id` int(10) NOT NULL COMMENT 'Location ID referencing Location Table',
  `department_id` int(10) NOT NULL COMMENT 'Department ID referencing Department Table',
  `event_id` int(10) NOT NULL COMMENT 'Resource ID referencing Resource Table',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT 'Enable Flag. Y = enable. N = disable',
  PRIMARY KEY (`id`),
  UNIQUE KEY `location_id` (`location_id`,`department_id`,`event_id`),
  KEY `department_id` (`department_id`),
  KEY `resource_id` (`event_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `login_param_config` */

DROP TABLE IF EXISTS `login_param_config`;

CREATE TABLE `login_param_config` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id for each Login Field',
  `device_type` varchar(50) NOT NULL COMMENT 'Device for which Login field applies like online,sms,mobile',
  `display_context` varchar(50) NOT NULL COMMENT 'Business App specific context (optional). Used only in case when same Business App has more than one contexts. ',
  `placement` smallint(6) NOT NULL COMMENT 'Order of display of Login fields. First Smallest and Last Greatest.',
  `param_table` varchar(200) NOT NULL COMMENT 'Table name from which Login field should be matched. Usually it is Customer table.',
  `param_column` varchar(200) NOT NULL COMMENT 'Column name to be matched with Login Field user value.',
  `java_reflection` varchar(100) DEFAULT NULL,
  `param_type` varchar(200) NOT NULL COMMENT 'Data type of the Login field',
  `login_type` varchar(200) NOT NULL COMMENT 'login = Reject if not matched with DB value; update = Just update the field to the db; label = No action required',
  `display_type` varchar(50) DEFAULT NULL COMMENT 'Html types like text, textarea, list, dropdown, checkbox, radio, multibox etc..',
  `display_title` varchar(500) NOT NULL DEFAULT '' COMMENT 'Title to be displayed for the Login field. TTS text for IVR',
  `field_notes` varchar(200) DEFAULT NULL COMMENT 'Any field notes to be displayed next to text box or other html elements. example:   (format: xxx-xxx-xxxx) for a phone field. Audio file name for IVR',
  `display_format` varchar(50) NOT NULL COMMENT 'Display format of the Login field',
  `display_size` smallint(6) NOT NULL COMMENT 'Usually Text box size. Minimum digits for IVR',
  `max_chars` smallint(6) NOT NULL COMMENT 'Specify max characters allowed on the login field. Maximum digits for IVR',
  `textarea_rows` smallint(6) NOT NULL COMMENT 'Usually textarea height',
  `textarea_cols` smallint(6) NOT NULL COMMENT 'Usually textarea width',
  `display_hint` varchar(100) DEFAULT NULL COMMENT 'for future - Hint for the Login Field; Usually displayed after Login field',
  `display_tooltip` varchar(200) DEFAULT NULL COMMENT 'for future - Tooltip for the Login Field; Usually displayed when Mouse is clicked on info icon for the field.',
  `empty_error_msg` varchar(200) DEFAULT NULL COMMENT 'Error message to be displayed if Login Field is left empty by the User. Audio file name for NO_INPUT for IVR',
  `invalid_error_msg` varchar(200) DEFAULT NULL COMMENT 'Error message to be displayed if Login Field is invalid data as entered by the User. Audio file name for NO_MATCH for IVR',
  `validate_required` varchar(1) NOT NULL DEFAULT 'Y' COMMENT 'Y = Required field; N = Optional field',
  `validation_rules` varchar(1000) NOT NULL COMMENT 'CSV list of validation rules. values: alpha, numeric, date, spl_chars, space, pound, hypen, single_quote',
  `validate_max_chars` smallint(6) NOT NULL COMMENT 'Specify max characters if Login field is String type',
  `validate_min_value` smallint(6) NOT NULL COMMENT 'Specify min value of Login field here if Login field is Integer type',
  `validate_max_value` smallint(6) NOT NULL COMMENT 'Specify max value of Login field here if Login field is Integer type',
  `list_labels` varchar(1000) NOT NULL DEFAULT '' COMMENT 'CSV list of labels if Login field is List, Radio, Checkbox, Multibox etc',
  `list_values` varchar(1000) NOT NULL DEFAULT '' COMMENT 'CSV list of values if Login field is List, Radio, Checkbox, Multibox etc',
  `list_initial_values` varchar(500) NOT NULL COMMENT 'CSV list of initial values if Login field is List, Radio, Checkbox, Multibox etc',
  `required` char(1) NOT NULL DEFAULT 'Y',
  `initial_value` varchar(500) NOT NULL,
  `validate_min_chars` smallint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table defines Login Fields';

/*Table structure for table `main` */

DROP TABLE IF EXISTS `main`;

CREATE TABLE `main` (
  `trans_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Transaction Id. auto_increment',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp entry',
  `device` varchar(20) DEFAULT NULL COMMENT 'Device name - online, sms, mobile, ivr, admin etc',
  `uuid` varchar(60) DEFAULT NULL COMMENT 'UUID for mobile apps',
  `ip_address` varchar(60) DEFAULT NULL COMMENT 'IP Address for online apps',
  `caller_id` varchar(60) DEFAULT NULL COMMENT 'Caller ID name & number for IVR & SMS apps',
  `username` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`trans_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `notify` */

DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` datetime NOT NULL COMMENT 'set current timestamp',
  `campaign_id` bigint(20) unsigned NOT NULL COMMENT 'default is 1',
  `call_now` char(1) DEFAULT 'N' COMMENT 'default is N',
  `emergency_notify` char(1) DEFAULT 'N' COMMENT 'default is N',
  `broadcast_mode` char(1) DEFAULT 'N' COMMENT 'default is N',
  `notify_status` int(4) DEFAULT NULL COMMENT 'default is 1',
  `event_id` int(10) NOT NULL,
  `location_id` int(10) NOT NULL,
  `seat_id` int(10) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `first_name` varchar(30) NOT NULL COMMENT 'customer.first_name',
  `middle_name` varchar(30) DEFAULT NULL COMMENT 'customer.middle_name',
  `last_name` varchar(30) NOT NULL COMMENT 'customer.last_name',
  `phone1` varchar(10) DEFAULT NULL COMMENT 'null',
  `phone2` varchar(10) DEFAULT NULL COMMENT 'null',
  `phone3` varchar(10) DEFAULT NULL COMMENT 'null',
  `home_phone` varchar(15) DEFAULT NULL COMMENT 'customer.home_phone',
  `work_phone` varchar(15) DEFAULT NULL COMMENT 'customer.work_phone',
  `cell_phone` varchar(15) DEFAULT NULL COMMENT 'customer.cell_phone',
  `cell_phone_prov` int(4) DEFAULT NULL COMMENT 'default is null',
  `email` varchar(1000) DEFAULT NULL COMMENT 'customer.email',
  `email_cc` varchar(1000) DEFAULT NULL COMMENT 'null',
  `email_bcc` varchar(1000) DEFAULT NULL COMMENT 'null',
  `lang_id` int(4) DEFAULT NULL COMMENT 'null',
  `notify_preference` int(4) DEFAULT NULL COMMENT 'null',
  `notify_by_phone` char(1) DEFAULT NULL COMMENT 'if home_phone is not null set to Y. Otherwise set to N',
  `notify_by_phone_confirm` char(1) DEFAULT NULL COMMENT 'if home_phone is not null set to Y. Otherwise set to N',
  `notify_by_sms` char(1) DEFAULT NULL COMMENT 'if cell_phone is not null set to Y. Otherwise set to N',
  `notify_by_sms_confirm` char(1) DEFAULT NULL COMMENT 'if cell_phone is not null set to Y. Otherwise set to N',
  `notify_by_email` char(1) DEFAULT NULL COMMENT 'if email is not null set to Y. Otherwise set to N',
  `notify_by_email_confirm` char(1) DEFAULT NULL COMMENT 'if email is not null set to Y. Otherwise set to N',
  `notify_by_push_notif` char(1) DEFAULT NULL COMMENT 'null',
  `notify_phone_status` int(4) DEFAULT NULL COMMENT 'null',
  `notify_sms_status` int(4) DEFAULT NULL COMMENT 'null',
  `notify_email_status` int(4) DEFAULT NULL COMMENT 'null',
  `notify_push_notification_status` int(4) DEFAULT NULL COMMENT 'null',
  `include_audio_1` varchar(1000) DEFAULT NULL COMMENT 'null',
  `include_audio_2` varchar(1000) DEFAULT NULL COMMENT 'null',
  `include_audio_3` varchar(1000) DEFAULT NULL COMMENT 'null',
  `include_audio_4` varchar(1000) DEFAULT NULL COMMENT 'null',
  `include_audio_5` varchar(1000) DEFAULT NULL COMMENT 'null',
  `attrib1` varchar(200) DEFAULT NULL,
  `attrib2` varchar(200) DEFAULT NULL,
  `attrib3` varchar(200) DEFAULT NULL,
  `attrib4` varchar(200) DEFAULT NULL,
  `attrib5` varchar(200) DEFAULT NULL,
  `attrib6` varchar(200) DEFAULT NULL,
  `attrib7` varchar(200) DEFAULT NULL,
  `attrib8` varchar(200) DEFAULT NULL,
  `attrib9` varchar(200) DEFAULT NULL,
  `attrib10` varchar(200) DEFAULT NULL,
  `attrib11` varchar(200) DEFAULT NULL,
  `attrib12` varchar(200) DEFAULT NULL,
  `attrib13` varchar(200) DEFAULT NULL,
  `attrib14` varchar(200) DEFAULT NULL,
  `attrib15` varchar(200) DEFAULT NULL,
  `attrib16` varchar(200) DEFAULT NULL,
  `attrib17` varchar(200) DEFAULT NULL,
  `attrib18` varchar(200) DEFAULT NULL,
  `attrib19` varchar(200) DEFAULT NULL,
  `attrib20` varchar(200) DEFAULT NULL,
  `notes` text,
  `do_not_notify` char(1) DEFAULT 'N' COMMENT 'N',
  `comment` text DEFAULT NULL COMMENT 'null',
  `delete_flag` char(1) DEFAULT 'N' COMMENT 'default is N',
  `due_date_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'appointment date time',
  PRIMARY KEY (`id`),
  KEY `campaign_id` (`campaign_id`),
  KEY `resource_id` (`event_id`),
  KEY `location_id` (`location_id`),
  KEY `service_id` (`seat_id`),
  KEY `customer_id` (`customer_id`),
  KEY `schedule_id` (`schedule_id`),
  CONSTRAINT `notify_ibfk_1` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `notify_email_status` */

DROP TABLE IF EXISTS `notify_email_status`;

CREATE TABLE `notify_email_status` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `notify_id` bigint(20) unsigned NOT NULL,
  `attempt_id` int(4) NOT NULL,
  `email` varchar(200) DEFAULT NULL,
  `email_status` int(4) NOT NULL,
  `email_timestamp` datetime NOT NULL,
  `number_of_email` int(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `notify_email_status_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `notify_phone_status` */

DROP TABLE IF EXISTS `notify_phone_status`;

CREATE TABLE `notify_phone_status` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `notify_id` bigint(20) unsigned NOT NULL,
  `attempt_id` int(4) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `call_status` int(4) NOT NULL,
  `call_timestamp` datetime NOT NULL,
  `seconds` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `notify_phone_status_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `notify_push_notification_status` */

DROP TABLE IF EXISTS `notify_push_notification_status`;

CREATE TABLE `notify_push_notification_status` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `notify_id` bigint(20) unsigned NOT NULL,
  `attempt_id` int(4) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `push_notification_status` int(4) NOT NULL,
  `push_notification_timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `notify_push_notification_status_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `notify_sms_status` */

DROP TABLE IF EXISTS `notify_sms_status`;

CREATE TABLE `notify_sms_status` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `notify_id` bigint(20) unsigned NOT NULL,
  `attempt_id` int(4) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `sms_status` int(4) NOT NULL,
  `sms_timestamp` datetime NOT NULL,
  `number_of_sms` int(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `notify_sms_status_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `outbound_phone_logs` */

DROP TABLE IF EXISTS `outbound_phone_logs`;

CREATE TABLE `outbound_phone_logs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` datetime NOT NULL,
  `call_id` bigint(20) unsigned NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `call_date` datetime DEFAULT NULL,
  `attempts` int(4) DEFAULT NULL,
  `pickups` int(4) DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `duration` int(4) DEFAULT NULL,
  `notify_id` bigint(20) unsigned NOT NULL,
  `result` varchar(200) DEFAULT NULL,
  `cause` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `notify_id` (`notify_id`),
  CONSTRAINT `outbound_phone_logs_ibfk_1` FOREIGN KEY (`notify_id`) REFERENCES `notify` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `privilege_page_mapping` */

DROP TABLE IF EXISTS `privilege_page_mapping`;

CREATE TABLE `privilege_page_mapping` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `privilege_id` bigint(20) unsigned NOT NULL,
  `page_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `privilege_id` (`privilege_id`),
  CONSTRAINT `privilege_page_mapping_ibfk_1` FOREIGN KEY (`privilege_id`) REFERENCES `access_privilege` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `procedure` */

DROP TABLE IF EXISTS `procedure`;

CREATE TABLE `procedure` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `procedure_name_online` varchar(100) NOT NULL DEFAULT '' COMMENT 'Procedure Name for Online apps',
  `procedure_name_mobile` varchar(100) NOT NULL DEFAULT '' COMMENT 'Procedure Name for Mobile apps',
  `procedure_name_sms` varchar(100) NOT NULL DEFAULT '' COMMENT 'Procedure Name for SMS apps',
  `procedure_name_ivr_tts` varchar(100) NOT NULL DEFAULT '' COMMENT 'Procedure Name for IVR TTS play',
  `procedure_name_ivr_audio` varchar(100) NOT NULL DEFAULT '' COMMENT 'Audio file name for IVR',
  `department_name_remind_sms` varchar(100) DEFAULT NULL COMMENT 'Procedure Name for SMS Reminder',
  `delete_flag` char(1) DEFAULT NULL COMMENT 'Soft delete. Y = Delete, N = Active',
  `placement` int(6) DEFAULT '1' COMMENT 'Order Placement',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `procedure_location` */

DROP TABLE IF EXISTS `procedure_location`;

CREATE TABLE `procedure_location` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `procedure_id` int(10) NOT NULL COMMENT 'Procedure Id - linked to Procedure Table',
  `location_id` int(10) NOT NULL COMMENT 'Location Id - linked to Location Table',
  `enable` char(1) NOT NULL DEFAULT 'Y' COMMENT 'Y = enable. N = disable',
  PRIMARY KEY (`id`),
  UNIQUE KEY `procedure_id` (`procedure_id`,`location_id`),
  KEY `location_id` (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `remind_status` */

DROP TABLE IF EXISTS `remind_status`;

CREATE TABLE `remind_status` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `status_key` varchar(200) NOT NULL,
  `display_status` varchar(200) NOT NULL,
  `notify_status` int(4) NOT NULL,
  `notify_email_status` int(4) NOT NULL,
  `notify_sms_status` int(4) NOT NULL,
  `notify_phone_status` int(4) NOT NULL,
  `display_icon` varchar(50) DEFAULT NULL,
  `need_appt` char(1) NOT NULL DEFAULT 'N',
  `placement` int(4) DEFAULT NULL,
  `delete_flag` char(1) DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*Table structure for table `reservation` */

DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `reservation` (
  `conf_number` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'confirmation number for appointment scheduling. Unique id for each row',
  `trans_id` bigint(20) unsigned NOT NULL COMMENT 'transaction id obtainted from main table',
  `schedule_id` bigint(20) NOT NULL COMMENT 'schedule id obtained from schedule table',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'timestamp',
  `appt_method` tinyint(4) NOT NULL COMMENT '1=online, 2=ivr, 3=admin, 4=iphone, 5=android, 6=ipad, 7=sms',
  `appt_type` tinyint(4) NOT NULL COMMENT '1=make, 2=cancel',
  `lang_code` varchar(5) NOT NULL DEFAULT 'us-en',
  `outlook_google_sync` varchar(1) NOT NULL DEFAULT 'N' COMMENT 'Y=synced with outlook or google, N=not synced',
  PRIMARY KEY (`conf_number`),
  KEY `trans_id` (`trans_id`),
  KEY `schedule_id` (`schedule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `reservationstatus` */

DROP TABLE IF EXISTS `reservationstatus`;

CREATE TABLE `reservationstatus` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each row',
  `status` varchar(50) NOT NULL,
  `status_val` int(10) NOT NULL DEFAULT '0',
  `imgname` varchar(20) DEFAULT NULL,
  `block_from_future_appts` char(1) NOT NULL DEFAULT 'N',
  `no_more_appts_until_date` date DEFAULT NULL,
  `allow_future_appts` char(1) NOT NULL DEFAULT 'Y',
  `placement` int(6) DEFAULT '1' COMMENT 'Placement Order',
  `delete_flag` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `resv_sys_config` */

DROP TABLE IF EXISTS `resv_sys_config`;

CREATE TABLE `resv_sys_config` (
  `id` int(10) NOT NULL DEFAULT '0',
  `max_appt_duration_days` tinyint(4) NOT NULL COMMENT 'maximum number days in advance the users can make appointment',
  `appt_delay_time_days` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'how many days after which appointments can be made. default is 0. default is 0.',
  `appt_delay_time_hrs` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'how many hours after which appointments can be made.',
  `max_no_future_appts_allowed` tinyint(4) NOT NULL DEFAULT '-1' COMMENT 'how many future appointments are allowed for the same customer. default is -1 which means unlimited appointments.',
  `record_msg` char(1) DEFAULT 'N' COMMENT 'record name/address/other info at the end for ivr? Y = yes, N = no',
  `display_company` char(1) NOT NULL DEFAULT 'N',
  `display_procedure` char(1) NOT NULL DEFAULT '' COMMENT 'procedure display. Y = display, N = hide',
  `display_location` char(1) NOT NULL DEFAULT '' COMMENT 'location display. Y = display, N = hide',
  `display_department` char(1) NOT NULL DEFAULT '' COMMENT 'department display. Y = display, N = hide',
  `display_event` char(1) NOT NULL DEFAULT '' COMMENT 'resource display. Y = display, N = hide',
  `display_seat` char(1) NOT NULL DEFAULT '' COMMENT 'service display. Y = display, N = hide',
  `display_comments` char(1) NOT NULL DEFAULT 'N',
  `comments_rows` int(11) NOT NULL DEFAULT '0',
  `comments_cols` int(11) NOT NULL DEFAULT '0',
  `audio_file_format` varchar(10) DEFAULT 'wav' COMMENT 'Audio file format for IVR. Default is wav file format.',
  `allow_asr` char(1) NOT NULL DEFAULT '' COMMENT 'Allow Speech Recognition. Y = allow. N = only dtmf.',
  `enforce_login` char(1) DEFAULT '' COMMENT 'Login Enforced Flag. Y = Reject Wrong Login; N = Accept Wrong Login as New Customer',
  `register_wrong_login` char(1) DEFAULT '' COMMENT 'Display Registration Page for Wrong Login? Y = Show a Register Page in case of Wrong Login; N = Donot show Register Page in case of Wrong Login',
  `display_credit_card_module` char(1) DEFAULT NULL COMMENT 'Flag to display credit card module. Y = display. N = do not display',
  `payment_gateway` tinyint(4) DEFAULT NULL COMMENT 'Payment Gateway. 1 = Paypal, 2 = Authorize .NET, etc.',
  `scheduler_closed` char(1) DEFAULT 'N',
  `no_funding` char(1) DEFAULT 'N',
  `funding_based_scheduler` char(1) DEFAULT 'N',
  `block_customer_enabled` char(1) DEFAULT 'N',
  `one_appt_per_term` char(1) NOT NULL DEFAULT 'N',
  `term_start_date` date NOT NULL,
  `term_end_date` date NOT NULL,
  `send_conf_email` char(1) NOT NULL DEFAULT 'Y',
  `send_cancel_email` char(1) NOT NULL DEFAULT 'Y',
  `send_reschd_email` char(1) NOT NULL DEFAULT 'Y',
  `cc_confirm_email` varchar(200) DEFAULT NULL,
  `cc_cancel_email` varchar(200) DEFAULT NULL,
  `cc_resource_confirm_email` char(1) NOT NULL DEFAULT 'N',
  `cc_resource_cancel_email` char(1) NOT NULL DEFAULT 'N',
  `skip_db_welcome` char(1) DEFAULT 'N',
  `appt_start_date` date DEFAULT '2014-09-01',
  `appt_end_date` date DEFAULT '2014-09-30',
  `restrict_appt_window` char(1) NOT NULL DEFAULT 'N',
  `display_screened` char(1) NOT NULL DEFAULT 'N',
  `login_first` char(1) DEFAULT 'Y',
  `transcribe` char(1) NOT NULL DEFAULT 'N',
  `transcribe_spelled_names` char(1) NOT NULL DEFAULT 'N',
  `display_loc_address` char(1) NOT NULL DEFAULT 'N',
  `display_max_no_seats` int(2) NOT NULL DEFAULT '100',
  `display_max_no_time_ivr` int(2) NOT NULL DEFAULT '100',
  `ivr_time_batch_size` int(2) NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Scheduler specific configuration is set using this table';

/*Table structure for table `resvstatus` */

DROP TABLE IF EXISTS `resvstatus`;

CREATE TABLE `resvstatus` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each row',
  `status` varchar(50) NOT NULL,
  `status_val` int(10) NOT NULL DEFAULT '0',
  `imgname` varchar(20) DEFAULT NULL,
  `block_from_future_appts` char(1) NOT NULL DEFAULT 'N',
  `no_more_appts_until_date` date DEFAULT NULL,
  `allow_future_appts` char(1) NOT NULL DEFAULT 'Y',
  `placement` int(6) DEFAULT '1' COMMENT 'Placement Order',
  `delete_flag` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `schedule` */

DROP TABLE IF EXISTS `schedule`;

CREATE TABLE `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(4) NOT NULL,
  `trans_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL DEFAULT '0',
  `procedure_id` bigint(20) NOT NULL DEFAULT '0',
  `location_id` bigint(20) NOT NULL,
  `department_id` bigint(20) NOT NULL DEFAULT '0',
  `event_id` bigint(20) NOT NULL,
  `event_date_time_id` bigint(20) unsigned NOT NULL,
  `seat_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) unsigned NOT NULL,
  `comments` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `seat` */

DROP TABLE IF EXISTS `seat`;

CREATE TABLE `seat` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `event_date_time_id` bigint(20) unsigned NOT NULL,
  `seat_number` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display_seat_number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `audio` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tts` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schedule_id` bigint(20) unsigned NOT NULL,
  `placement` int(4) unsigned NOT NULL,
  `reserved` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'N',
  `category_1` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_2` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_3` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_4` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_5` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `row_id` int(4) DEFAULT NULL,
  `column_id` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_ref_id` (`event_date_time_id`,`seat_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `sms_config` */

DROP TABLE IF EXISTS `sms_config`;

CREATE TABLE `sms_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id',
  `client_id` int(11) NOT NULL COMMENT 'Foregin Key to Client table',
  `account_sid` varchar(50) DEFAULT 'AC4458b0b635416cba4238effa76e78e84',
  `auth_token` varchar(50) DEFAULT '31056ad9bdd97443ec55231d6c966881',
  `sms_phone` varchar(15) NOT NULL,
  `event_id` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `tokens` */

DROP TABLE IF EXISTS `tokens`;

CREATE TABLE `tokens` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each token',
  `device` varchar(50) NOT NULL,
  `session_id` varchar(100) DEFAULT NULL,
  `client_code` varchar(20) NOT NULL,
  `expiry_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `token` varchar(50) NOT NULL,
  `customer_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `trans_state` */

DROP TABLE IF EXISTS `trans_state`;

CREATE TABLE `trans_state` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `trans_id` bigint(20) unsigned NOT NULL COMMENT 'Transaction Id linked to main table',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp',
  `state` int(4) NOT NULL COMMENT 'State value. example: 1 = login page complete. 2 = Book Appointment. 3 = Email Sent. 4 = SMS sent. 9 = Confirmation Page Display.',
  PRIMARY KEY (`id`),
  KEY `trans_id` (`trans_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `voice_msg` */

DROP TABLE IF EXISTS `voice_msg`;

CREATE TABLE `voice_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `schedule_id` bigint(20) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customer_id` bigint(20) unsigned NOT NULL COMMENT 'Customer Id linked to customer table',
  `display_flag` char(1) DEFAULT 'Y',
  `file_path` varchar(500) NOT NULL DEFAULT '/ivr_recordings/mp3/ivrappt',
  `file_name` varchar(200) DEFAULT NULL,
  `encrypted` char(1) DEFAULT 'N',
  `record_duration` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


DELIMITER $$
DROP PROCEDURE IF EXISTS `reservation_overview`$$

CREATE PROCEDURE `reservation_overview`(IN `start_date` VARCHAR(64), IN `end_date` VARCHAR(64), OUT `status_code` INT, OUT `status_message` VARCHAR(128))
BEGIN
	select distinct event_name,location_name,edt1.date, edt1.time,edt1.num_seats, 
	  (select count(*) from seat s2, event_date_time edt2 where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id>0) as bookedSeats,
	  (select count(*) from seat s2, event_date_time edt2 where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id=0 and reserved='N') as openSeats,
	  (select count(*) from seat s2, event_date_time edt2 where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id=0 and reserved='Y') as resvered,
	  edt1.id as eventDateTimeId
	from event_date_time edt1,seat s1, location l, event e where edt1.date >=start_date and edt1.date<=end_date and edt1.id = s1.event_date_time_id 
	and  edt1.event_id = e.event_id and edt1.location_id = l.id and edt1.enable='Y' order by edt1.date, edt1.time;
END$$
DELIMITER ;

-- 17th oct
DROP TABLE IF EXISTS `dynamic_template_placeholder`;

CREATE TABLE `dynamic_template_placeholder` (
id  int(4) NOT NULL AUTO_INCREMENT,
category varchar(200) COLLATE utf8_unicode_ci NOT NULL,
table_name varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
table_column varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
`alias_name` varchar(600) COLLATE utf8_unicode_ci DEFAULT NULL,
`type` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
place_holder varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

commit;
	