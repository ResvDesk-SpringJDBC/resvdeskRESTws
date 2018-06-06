alter table login_param_config add column storage_size int(11) default 0;
alter table login_param_config add column storage_type varchar(50) NULL comment 'first / last  / full / prefix0 / postfix0';


-- Balaji
-- 02nd Dec 2015
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='tooltip_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='report_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='table_printview_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='customer_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='resvsearch_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='blocked_customer_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(cu.contact_phone,3) , \"-\" , mid(cu.contact_phone,4,3) , \"-\", right(cu.contact_phone,4))' where category='resv_search_fields' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(no.home_phone,3) , \"-\" , mid(no.home_phone,4,3) , \"-\", right(no.home_phone,4))' where category='resv_reminder_results' and alias_name='homePhone';

update dynamic_table_items set table_column='concat( left(c.contact_phone,3) , \"-\" , mid(c.contact_phone,4,3) , \"-\", right(c.contact_phone,4))' where category='inboundcalls_results' and alias_name='contactPhone';
update dynamic_table_items set table_column='concat( left(c.home_phone,3) , \"-\" , mid(c.home_phone,4,3) , \"-\", right(c.home_phone,4))' where category='inboundcalls_results' and alias_name='homePhone';
update dynamic_table_items set table_column='concat(left(nps.phone,3) , \'-\' , mid(nps.phone,4,3) , \'-\', right(nps.phone,4))' where category='outboundcalls_results' and alias_name='phone';

update dynamic_table_items set hide='Y' where category='tooltip_results' and alias_name='seatNumber';
insert  into `dynamic_table_items`(`category`,`table_name`,`table_column`,`alias_name`,`java_reflection`,`title`,`hide`,`type`,`placement`) values ('tooltip_results','customer cu','cu.account_number','accountNumber','accountNumber','AccountNumber','N','varchar',2);
update dynamic_table_items set hide='Y' where category='table_printview_results' and alias_name='diplaySeatNumber';
update dynamic_table_items set hide='Y' where category='table_printview_results' and alias_name='seatNumber';

update dynamic_table_items set hide='Y' where category='report_results' and alias_name='procedureName';
update dynamic_table_items set hide='Y' where category='report_results' and alias_name='companyName';
update dynamic_table_items set hide='Y' where category='report_results' and alias_name='departmentName';


insert  into `dynamic_table_items`(`category`,`table_name`,`table_column`,`alias_name`,`java_reflection`,`title`,`hide`,`type`,`placement`) values ('table_printview_results','customer cu','cu.account_number','accountNumber','accountNumber','AccountNumber','N','varchar',12);
insert  into `dynamic_table_items`(`category`,`table_name`,`table_column`,`alias_name`,`java_reflection`,`title`,`hide`,`type`,`placement`) values ('report_results','customer cu','cu.account_number','accountNumber','accountNumber','AccountNumber','N','varchar',10);
-- add entry in login_param_table with field account_number for device admin.


-- Murali
-- 04 Dec 2015
alter table schedule add column `screened` char(1) NOT NULL DEFAULT 'N';

insert  into `dynamic_table_items`(`category`,`table_name`,`table_column`,`alias_name`,`java_reflection`,`title`,`hide`,`type`,`placement`) values 
('tooltip_results','schedule sc','sc.screened','screened','screened','Screened','Y','char',12);

-- Balaji
-- 11 Dec 2015
alter table event_date_time add column booked_seats bigint(20) default 0;

ALTER TABLE `ivr_vxml`
	ADD COLUMN `campaign_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' AFTER `page_name`;



-- Murali
-- 15 Dec 2015
update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.date >=? and edt.date<=? and edt.event_id=? and edt.location_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_event_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.date >=? and edt.date<=? and edt.location_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.date >=? and edt.date<=? and edt.event_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_event_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.date >=? and edt.date<=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_event_not_selected';


-- Balaji
-- 15 Dec 2015
update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.id in (%EDT_IDS%) and edt.date >=? and edt.date<=? and edt.event_id=? and edt.location_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_event_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.id in (%EDT_IDS%) and edt.date >=? and edt.date<=? and edt.location_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.id in (%EDT_IDS%) and edt.date >=? and edt.date<=? and edt.event_id=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_event_selected';

update `dynamic_sql` set `sql_query`='select %DYNAMIC_SELECT% from %DYNAMIC_FROM% where edt.id in (%EDT_IDS%) and edt.date >=? and edt.date<=? and sc.status in (%SCH_STATUS%) and r.schedule_id = sc.id and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id and sc.department_id=d.id and sc.event_id =e.event_id and sc.event_date_time_id = edt.id and sc.seat_id=s.id and sc.customer_id = cu.id order by edt.date,edt.time, s.placement asc' 
where `category`='resvreport_loc_event_not_selected';

-- Balaji
-- 20 Dec 2015
alter table notify add column event_date_time_id bigint(20) after `seat_id`;

-- Balaji
-- 28 Dec 2015
alter table resv_sys_config add column run_phone_type_lookup char(1) not null default 'N';



-- Balaji 
-- 10th Jan 2016
DROP TABLE IF EXISTS `ivr_calls`;
CREATE TABLE `ivr_calls` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  `customer_id` bigint(20) unsigned DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `event_date_time_id` bigint(20) DEFAULT NULL,
  `location_id` int(20) DEFAULT NULL,
  `seat_id` bigint(20) DEFAULT NULL,
  `conf_num` bigint(20) unsigned DEFAULT NULL,
  `appt_type` tinyint(4) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `seconds` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trans_id` (`trans_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `file_upload_history`;
DROP TABLE IF EXISTS `file_upload`;

CREATE TABLE `file_upload` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `file_type` varchar(20) NOT NULL,
  `file_name` varchar(500) DEFAULT NULL,
  `encrypted` char(1) DEFAULT 'N',
  `encryption_type` varchar(50) DEFAULT NULL,
  `encryption_key_public` text,
  `encryption_key_private` text,
  `ignore_rows` int(4) NOT NULL DEFAULT '0',
  `max_file_size_kb` int(11) DEFAULT NULL,
  `mapping_script` varchar(500) DEFAULT NULL,
  `run_phone_type_lookup` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fu` (`file_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `file_upload_history` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `file_upload_id` int(10) NOT NULL,
  `upload_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_records` int(4) DEFAULT NULL,
  `success_records` int(4) DEFAULT NULL,
  `failure_records` int(4) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `file_upload_id` (`file_upload_id`),
  CONSTRAINT `file_upload_history_ibfk_1` FOREIGN KEY (`file_upload_id`) REFERENCES `file_upload` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `file_upload` (`file_type`, `file_name`, `encrypted`, `encryption_type`, `encryption_key_public`, `encryption_key_private`, `ignore_rows`, `max_file_size_kb`, `mapping_script`, `run_phone_type_lookup`) values('CSV',NULL,'N','PGP','mQGiBFIULD8RBADw310a0eHEUjtN4L8AibUTmIpJxAjJg7dPWU8Elr6EbB9OHw1d\nl19HDR7rBAXJprlYiswTvDjxIN1rOeAFOnaw4lBT6Gq9S6BkgM6a0KfENePs37wL\nsnwYHRdzzrrHrS2Yi/WPH7jwpgfeOYW3oT/BjiLZZSIVs2kx6KVAtZ3E2wCg/x9S\n36j+bevqMNIP+j+EFjVZ+tED/RY24aB+4/T8PeNjc9COJOtB/v2+s4TL56uSC297\nalkkczgItqFKqugAZMFxPmbgYldPEn8u0q79QdIx8mh5tKGZoba9hSbU+YkUS8Ky\nLltZP043K4wFh2RadVNLSpYg3ZEJDKIQpCpKLz3RFFNuglQzHzl8pmT72kiZMqc6\n5JCoA/913NXC1A8TUEGHql3ub/kc/XVlWuB1aSUy6e2J9DKlt1X1D+NXtnKOcOxV\n12wy0OJ0C341eYeS8vyDsOJXrZ0//fV6E6iFawgRYR6Z0WJdQN+ZFYKRlURIAS19\nCwA+sa3IsIWv7WFBJmE2v8yD1tMQKYKXDdvF5PWUOVzU5Wt7VbQsQW5hbnRoYSBM\nYWtzaG1hbmFuIDxhbmFudGhhQGl0ZnJvbnRkZXNrLmNvbT6JAE4EEBECAA4FAlIU\nLD8ECwMCAQIZAQAKCRAKEl6xWHNZAe71AKCxMzdMtJTN3w6moBXxL0n1VYJrfgCg\nmGswdQNWYV8isiMvsU91WbPLqIO5Ag0EUhQsPxAIAPZCV7cIfwgXcqK61qlC8wXo\n+VMROU+28W65Szgg2gGnVqMU6Y9AVfPQB8bLQ6mUrfdMZIZJ+AyDvWXpF9Sh01D4\n9Vlf3HZSTz09jdvOmeFXklnN/biudE/F/Ha8g8VHMGHOfMlm/xX5u/2RXscBqtNb\nno2gpXI61Brwv0YAWCvl9Ij9WE5J280gtJ3kkQc2azNsOA1FHQ98iLMcfFstjvbz\nySPAQ/ClWxiNjrtVjLhdONM0/XwXV0OjHRhs3jMhLLUq/zzhsSlAGBGNfISnCnLW\nhsQDGcgHKXrKlQzZlp+r0ApQmwJG0wg9ZqRdQZ+cfL2JSyIZJrqrol7DVekyCzsA\nAgIH/jxlE4q59Uhi+sham/dmlOgf20Vetmyuzo4wYIXHfCnder6SyOHFkaDfxxW5\n1SsYHjI9Zto3la3W0jw4jEbss6pG5YgVYD8rqvAMdjkTjiUAPTvz3rydbx4pWy2u\n+zhSo8ejj4ROGHWOxwAtCALI6LCjKpgvUVuRqZ9UWX1AICrKGhYa/yTHsqGqbKXa\nG+Yj+XI0FZ1kIEtojhlpnmN4w4kViay0eKszstkX7Edk2Thw8lvPTwWU8ws3sSUY\nn1D3Uh9vVPihz9dr8JC3rnLQTJFWaxKQVykOWqIg5oPD0PbNhxcKdFfBU3lBreid\nC4xxwz3R/eKHxLF0kofVyF+SowSJAEYEGBECAAYFAlIULD8ACgkQChJesVhzWQEG\npwCfT2NpCH17jtu8+AlmYVYTGKFZ/LcAoL3JrOeDghbu8h47q44hy/QhhMj4\n=Vgy4','lQHPBFIULD8RBADw310a0eHEUjtN4L8AibUTmIpJxAjJg7dPWU8Elr6EbB9OHw1d\nl19HDR7rBAXJprlYiswTvDjxIN1rOeAFOnaw4lBT6Gq9S6BkgM6a0KfENePs37wL\nsnwYHRdzzrrHrS2Yi/WPH7jwpgfeOYW3oT/BjiLZZSIVs2kx6KVAtZ3E2wCg/x9S\n36j+bevqMNIP+j+EFjVZ+tED/RY24aB+4/T8PeNjc9COJOtB/v2+s4TL56uSC297\nalkkczgItqFKqugAZMFxPmbgYldPEn8u0q79QdIx8mh5tKGZoba9hSbU+YkUS8Ky\nLltZP043K4wFh2RadVNLSpYg3ZEJDKIQpCpKLz3RFFNuglQzHzl8pmT72kiZMqc6\n5JCoA/913NXC1A8TUEGHql3ub/kc/XVlWuB1aSUy6e2J9DKlt1X1D+NXtnKOcOxV\n12wy0OJ0C341eYeS8vyDsOJXrZ0//fV6E6iFawgRYR6Z0WJdQN+ZFYKRlURIAS19\nCwA+sa3IsIWv7WFBJmE2v8yD1tMQKYKXDdvF5PWUOVzU5Wt7Vf8DAwI7a3hFBx4t\ncmAJrFYFY6kWowO0IoMYZoGacanryMCb7ogB/wlcwgg1v7QsQW5hbnRoYSBMYWtz\naG1hbmFuIDxhbmFudGhhQGl0ZnJvbnRkZXNrLmNvbT6dAlEEUhQsPxAIAPZCV7cI\nfwgXcqK61qlC8wXo+VMROU+28W65Szgg2gGnVqMU6Y9AVfPQB8bLQ6mUrfdMZIZJ\n+AyDvWXpF9Sh01D49Vlf3HZSTz09jdvOmeFXklnN/biudE/F/Ha8g8VHMGHOfMlm\n/xX5u/2RXscBqtNbno2gpXI61Brwv0YAWCvl9Ij9WE5J280gtJ3kkQc2azNsOA1F\nHQ98iLMcfFstjvbzySPAQ/ClWxiNjrtVjLhdONM0/XwXV0OjHRhs3jMhLLUq/zzh\nsSlAGBGNfISnCnLWhsQDGcgHKXrKlQzZlp+r0ApQmwJG0wg9ZqRdQZ+cfL2JSyIZ\nJrqrol7DVekyCzsAAgIH/jxlE4q59Uhi+sham/dmlOgf20Vetmyuzo4wYIXHfCnd\ner6SyOHFkaDfxxW51SsYHjI9Zto3la3W0jw4jEbss6pG5YgVYD8rqvAMdjkTjiUA\nPTvz3rydbx4pWy2u+zhSo8ejj4ROGHWOxwAtCALI6LCjKpgvUVuRqZ9UWX1AICrK\nGhYa/yTHsqGqbKXaG+Yj+XI0FZ1kIEtojhlpnmN4w4kViay0eKszstkX7Edk2Thw\n8lvPTwWU8ws3sSUYn1D3Uh9vVPihz9dr8JC3rnLQTJFWaxKQVykOWqIg5oPD0PbN\nhxcKdFfBU3lBreidC4xxwz3R/eKHxLF0kofVyF+SowT/AwMC60PRiO6YxuZgv2V6\ngL0t1Hu0eNhw+pu4uS/ztkkzNjJM9Dk5heDvABslI4NFeYZHQHfVw80zxOI0zkk8\nSTRYUg==\n=LOSr','1','107400',NULL,'N');


-- Balaji 
-- 23th Jan 2016
UPDATE dynamic_sql set sql_query='select %DYNAMIC_SELECT% from ivr_calls iv LEFT JOIN customer c ON iv.customer_id=c.id LEFT JOIN event e ON iv.event_id=e.event_id LEFT JOIN location l on iv.location_id=l.id where date(iv.start_time) >= :startTime AND date(iv.start_time) <= :endTime and c.contact_phone=:callerId' where category='inbound_calls_callerId';
UPDATE dynamic_sql set sql_query='select %DYNAMIC_SELECT% from ivr_calls iv LEFT JOIN customer c ON iv.customer_id=c.id LEFT JOIN event e ON iv.event_id=e.event_id LEFT JOIN location l on iv.location_id=l.id where date(iv.start_time) >= :startTime AND date(iv.start_time) <= :endTime' where category='inbound_calls_non_callerId';


insert into `i18n_display_page_content` (`id`, `device`, `lang`, `message_key`, `message_value`) values('45','online','us-en','LANDING_PAGE_CONTENT','  <div class=\"page_container\">\r\n    <!-- Page header starts -->\r\n      <div class=\"page_header\">\r\n        <h1>Welcome </h1>\r\n        <div class=\"links\"><a href=\"index.html\" class=\"homelink\" title=\"Home\"></a><a href=\"javascript:bookmarksite(\'http://www.FedExHandlerJobs.com\')\" class=\"bookmark\" title=\"Bookmark Us\"></a></div>\r\n      </div>\r\n      <p>At our locations around the U.S., FedEx Express handlers move millions of packages each day. Our handlers are the driving force behind our business, making absolutely sure packages are loaded safely and securely into FedEx Express planes and trucks.</p>\r\n      <h2 class=\"align_center\">We invite YOU to join this elite team!</h2>\r\n    <!-- Page header ends -->\r\n    <!-- Page container starts -->\r\n    <div class=\"page\">\r\n      <div class=\"leftContainer\">\r\n        <p>Our permanent part-time positions in Memphis, Tenn., offer tuition assistance and the following great benefits:</p>\r\n        <ul class=\"benefits\">\r\n          <li>Excellent starting wage\r\n            <ul>\r\n              <li>Handler pay is $12.26</li>\r\n              <li>Material Handler pay is $13.00</li>\r\n            </ul>\r\n          </li>\r\n          <li>Paid vacation and holidays</li>\r\n          <li>Medical, dental, vision, life insurance</li>\r\n          <li>Retirement benefits</li>\r\n          <li>Discounted travel and shipping</li>\r\n        </ul>\r\n        <p><strong>Requirements</strong></p>\r\n        <ul class=\"requirements\">\r\n          <li>You must be at least 18 years of age</li>\r\n          <li>You must be legally authorized to work in the United States</li>\r\n          <li>Successfully pass a urine drug screen</li>\r\n          <li>Successfully pass a criminal background check completed prior to employment</li>\r\n          <li>Be able to lift 75 lbs. and maneuver weight above 75 lbs. with the proper equipment and/or assistance from another person</li>\r\n          <li>FedEx Hub handles USPS product. Therefore, the following applies: Males born after Dec. 31, 1959, who are required to register for Selective Service must have a Selective Service Registration number (www.sss.gov). Individuals must have resided in the United States during the previous five years (some exceptions apply such as: active duty in U.S. Uniformed Military Service, trailing spouse or dependent of someone working for the U.S. government (military or civilian); missionary; student attending school in a foreign country; Peace Corps participant; employee of a U.S. based employer/company or other extraordinary circumstances.)</li>\r\n        </ul>\r\n      </div>\r\n      <div class=\"rightContainer\">\r\n        <p class=\"Req_header\"><strong>Qualifications</strong></p>\r\n        <p>If you are interested in applying, please check your qualifications below and set up your own appointment.</p>\r\n        <p>Click here to view list of all scheduled application sessions.</p>\r\n        <p class=\"Req_header\">Starting November 9, 2015 through December 31, 2015 we will take walk-in applications at the following locations:</p>\r\n        <p class=\"padLeft20\">4009 Airways Blvd, Module N, Memphis, TN 38118<br />\r\nMon-Fri from 9 am to 3 pm</p>\r\n        <p class=\"padLeft20\">and</p>\r\n        <p class=\"padLeft20\">Memphis Recruitment Center<br />\r\n2874 Airport Business Drive; Bldg. D; Memphis, TN 38118<br />\r\nMon-Wed from 8:30 am to 11 am</p>\r\n		<p>New time slots are loaded monthly, if you cannot secure a seat or register for an appointment, please feel free to use the walk-in locations for submitting your application. Thanks for choosing FedEx as your employer, we look forward to you joining our team!</p>\r\n        <div class=\"formContainer\">\r\n      	  <form action=\"loginAfterLanding.html\" method=\"post\">\r\n          	<p class=\"align_center\"><input type=\"submit\" value=\"Continue\"></p>\r\n          </form>\r\n        </div>\r\n      </div>\r\n      <div class=\"clearAll\"></div>\r\n    </div>\r\n    <!-- Page container ends -->\r\n  </div>');
insert  into `dynamic_table_items`(`category`,`table_name`,`table_column`,`alias_name`,`java_reflection`,`title`,`hide`,`type`,`placement`) values ('report_results','customer cu','cu.attrib1','attrib1','attrib1','Attribute1','Y','varchar',11);

CREATE TABLE `display_time_lookup` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `time` varchar(20) NOT NULL,
  `display_time_online` varchar(200) NULL,
  `display_time_tts` varchar(200) NULL,
  `dislay_time_audio` varchar(200) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dob scripts.
-- balaji 25/08/2016
insert  into `login_param_config`(`device_type`,`display_context`,`placement`,`param_table`,`param_column`,`java_reflection`,`param_type`,`login_type`,`display_type`,`display_title`,`field_notes`,`display_format`,`display_size`,`max_chars`,`textarea_rows`,`textarea_cols`,`display_hint`,`display_tooltip`,`empty_error_msg`,`invalid_error_msg`,`validate_required`,`validation_rules`,`validate_max_chars`,`validate_min_value`,`validate_max_value`,`list_labels`,`list_values`,`list_initial_values`,`required`,`ivr_min_digits`,`ivr_max_digits`,`ivr_login_param_audio`,`ivr_login_param_tts`,`initial_value`,`validate_min_chars`,`storage_size`,`storage_type`) values 
('admin','',4,'customer','dob','dob','string','authenticate','textbox-2-2-4','LOGIN_DOB',NULL,'',10,8,0,0,NULL,NULL,'LOGIN_DOB_EMPTY_ERROR','LOGIN_DOB_INVALID_ERROR','Y','dob',8,8,0,'','','','Y',NULL,NULL,NULL,NULL,'',0,0,NULL);
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values 
('admin','us-en','LOGIN_DOB','DOB'),
('admin','us-en','LOGIN_DOB_EMPTY_ERROR','Please enter DOB'),
('admin','us-en','LOGIN_DOB_INVALID_ERROR','Please enter a valid DOB');



