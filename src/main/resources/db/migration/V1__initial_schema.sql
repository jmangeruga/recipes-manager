CREATE TABLE IF NOT EXISTS `recipe`
(
    `id`            INT             NOT NULL    AUTO_INCREMENT,
    `business_id`   BINARY(16)      NOT NULL    UNIQUE,
    `title`         VARCHAR(100)    NOT NULL,
    `servings`      SMALLINT        NOT NULL,
    `owner_id`      VARCHAR(36)     NOT NULL,
    `created_at`    DATETIME        NOT NULL,
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `ingredient`
(
    `id`            INT             NOT NULL    AUTO_INCREMENT,
    `name`          VARCHAR(3000)   NOT NULL,
    `type`          VARCHAR(20)     NOT NULL,
    `recipe_id`     INT             NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `recipe_fk` (`recipe_id`) REFERENCES `recipe` (`id`)
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `instruction`
(
    `id`            INT             NOT NULL    AUTO_INCREMENT,
    `description`   VARCHAR(3000)   NOT NULL,
    `recipe_id`     INT             NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `recipe_fk` (`recipe_id`) REFERENCES `recipe` (`id`)
)
ENGINE = InnoDB;