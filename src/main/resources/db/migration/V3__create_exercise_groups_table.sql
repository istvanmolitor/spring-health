CREATE TABLE exercise_groups
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_exercise_groups_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE exercises
    ADD COLUMN exercise_group_id BIGINT NULL AFTER description,
    ADD CONSTRAINT fk_exercises_exercise_group
        FOREIGN KEY (exercise_group_id) REFERENCES exercise_groups (id);
