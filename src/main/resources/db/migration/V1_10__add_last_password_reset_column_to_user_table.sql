ALTER TABLE `users` DROP COLUMN `password_updated`;
ALTER TABLE `users` ADD COLUMN `last_password_reset` datetime;
