-- initial schema migration script for geronimo

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `text` varchar(255) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKowtlim26svclkatusptbgi7u1` (`author_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `messages_answers`;
CREATE TABLE `messages_answers` (
  `message_id` bigint(20) NOT NULL,
  `answers_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_kepav286vw3q8dn1rilu4jj6b` (`answers_id`),
  KEY `FKgscg1xp1o1enyy8npf1m57dh` (`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `messages_likes`;
CREATE TABLE `messages_likes` (
  `message_id` bigint(20) NOT NULL,
  `likes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`message_id`,`likes_id`),
  KEY `FK1rgxms33f51l5op9iqpq89ux6` (`likes_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `messages_reblogs`;
CREATE TABLE `messages_reblogs` (
  `message_id` bigint(20) NOT NULL,
  `reblogs_id` bigint(20) NOT NULL,
  PRIMARY KEY (`message_id`,`reblogs_id`),
  KEY `FK35fv13h8h7ouqbgsrduyxph5n` (`reblogs_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `date_of_birth` datetime DEFAULT NULL,
  `picture` longblob,
  `status` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `users_followers`;
CREATE TABLE `users_followers` (
  `user_id` bigint(20) NOT NULL,
  `followers_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`followers_id`),
  KEY `FKstn0wiml6td6is3rw2751jy7v` (`followers_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `users_following`;
CREATE TABLE `users_following` (
  `user_id` bigint(20) NOT NULL,
  `following_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`following_id`),
  KEY `FKrfqb0kmfo2jv9xa0mw7e9euwg` (`following_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `users_messages`;
CREATE TABLE `users_messages` (
  `user_id` bigint(20) NOT NULL,
  `messages_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_9ybe7v51r6kklm7ecxyu5glp` (`messages_id`),
  KEY `FK3tpfu7mudlhpcrh6xufdkexha` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
