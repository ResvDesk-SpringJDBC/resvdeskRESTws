insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1087','NO_APPT_ALL_LOC','Failed in NO_APPT_ALL_LOC','Failed in NO_APPT_ALL_LOC API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1088','NO_APPT_SELECTED_LOCATION','Failed in NO_APPT_SELECTED_LOCATION','Failed in NO_APPT_SELECTED_LOCATION API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1089','NO_APPT_SELECTED_EVENT','Failed in NO_APPT_SELECTED_EVENT','Failed in NO_APPT_SELECTED_EVENT API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1090','NO_APPT_SELECTED_DATE','Failed in NO_APPT_SELECTED_DATE','Failed in NO_APPT_SELECTED_DATE API',NULL,'N');
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1091','NO_APPT_SELECTED_TIME','Failed in NO_APPT_SELECTED_TIME','Failed in NO_APPT_SELECTED_TIME API',NULL,'N');

-- 7th Jan 2016
-- Murali 
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1092','UPDATE_SCREENED_STAUS','Failed in UPDATE_SCREENED_STAUS','Failed in UPDATE_SCREENED_STAUS API',NULL,'N');


-- 8th Jan 2016
-- Murali 
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1093','UPDATE_SEAT_RESERVED_STAUS','Failed in UPDATE_SEAT_RESERVED_STAUS','Failed in UPDATE_SEAT_RESERVED_STAUS API',NULL,'N');

insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1094','SAVE_UPDATE_IVR_CALL','Failed in saveOrUpdate IVR Call','Failed in saveOrUpdate IVR Call',NULL,'N');

-- 10th jan 2016
insert into `error` (`id`, `error_code`, `error_message`, `error_description`, `error_vxml`, `send_alert`) values('1095','PHONE_LOG_UPLOAD','Failed in phonelog upload csv','Failed in phonelog upload csv',NULL,'N');

 -- 30th JAN 2016
-- Balaji  
CREATE TABLE `social_media_config` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(11) DEFAULT NULL,
  `social_media_type` varchar(50) NOT NULL COMMENT 'Twitter or FaceBook',
  `consumer_key` varchar(200) NOT NULL,
  `consumer_secret` varchar(200) NOT NULL,
  `access_token` varchar(200) NOT NULL,
  `access_token_secret` varchar(200) NOT NULL,
  `enable` char(1) DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;