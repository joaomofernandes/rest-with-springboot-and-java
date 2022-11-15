CREATE TABLE IF NOT EXISTS `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `author` varchar(25) NOT NULL,
  `release_date` DATE NOT NULL,
  `genre` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
);