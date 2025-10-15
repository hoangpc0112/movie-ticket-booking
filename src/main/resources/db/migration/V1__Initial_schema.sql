CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(255) DEFAULT NULL,
  `age` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `email_otp` VARCHAR(255) DEFAULT NULL,
  `first_name` VARCHAR(255) DEFAULT NULL,
  `last_name` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `movies` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `actors` VARCHAR(255) NOT NULL,
  `banner_url` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `director` VARCHAR(255) NOT NULL,
  `duration` INT NOT NULL,
  `genre` VARCHAR(255) NOT NULL,
  `keywords` VARCHAR(255) NOT NULL,
  `language` VARCHAR(255) NOT NULL,
  `now_showing` BIT(1) NOT NULL,
  `poster_url` VARCHAR(255) NOT NULL,
  `rated` VARCHAR(255) NOT NULL,
  `release_date` VARCHAR(255) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `trailer_url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bills` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_created` VARCHAR(255) NOT NULL,
  `total_price` INT NOT NULL,
  `user_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk8vs7ac9xknv5xp18pdiehpp1` (`user_id`),
  CONSTRAINT `FKk8vs7ac9xknv5xp18pdiehpp1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `comments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `commenter` VARCHAR(255) NOT NULL,
  `content` TEXT NOT NULL,
  `movie_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr1xv5xvew7k2aed5qu5lci3kt` (`movie_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr1xv5xvew7k2aed5qu5lci3kt` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `foods` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `food_name` VARCHAR(255) NOT NULL,
  `quantity` INT NOT NULL,
  `bill_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2wbx4ngw1a5vodwixdflaimsv` (`bill_id`),
  CONSTRAINT `FK2wbx4ngw1a5vodwixdflaimsv` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tickets` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date` VARCHAR(255) NOT NULL,
  `movie_title` VARCHAR(255) NOT NULL,
  `seat_label` VARCHAR(255) NOT NULL,
  `seat_no` INT NOT NULL,
  `time` VARCHAR(255) NOT NULL,
  `bill_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaqpjta99er6ahhyyq5689cyw4` (`bill_id`),
  CONSTRAINT `FKaqpjta99er6ahhyyq5689cyw4` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;