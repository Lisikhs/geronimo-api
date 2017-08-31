DELETE FROM users_followers;
DELETE FROM users_following;

DROP TABLE users_followers;
DROP TABLE users_following;

CREATE TABLE `followers_following` (
  `follower_id` bigint(20) NOT NULL,
  `following_id` bigint(20) NOT NULL,
  PRIMARY KEY (`follower_id`,`following_id`),
  KEY `FKstn0wiml6td6is3rw2751jy7v` (`following_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
