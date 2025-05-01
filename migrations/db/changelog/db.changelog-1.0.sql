--liquibase formatted sql

--changeset Alexandr-Kokorin:1
CREATE TABLE IF NOT EXISTS global_permission
(
    id   BIGSERIAL NOT NULL,
    name TEXT      NOT NULL,

    PRIMARY KEY (id)
);

--changeset Alexandr-Kokorin:2
CREATE TABLE IF NOT EXISTS application_user
(
    id                      BIGSERIAL NOT NULL,
    email                   TEXT      NOT NULL UNIQUE,
    display_name            TEXT      NOT NULL,
    hashed_password         TEXT      NOT NULL,
    global_permission_id    BIGINT    NOT NULL REFERENCES global_permission (id),

    PRIMARY KEY (id)
);

--changeset Alexandr-Kokorin:3
CREATE TABLE IF NOT EXISTS bank_card
(
    id                      BIGSERIAL NOT NULL,
    application_user_id     BIGINT    NOT NULL REFERENCES application_user (id) ON DELETE CASCADE,
    number                  TEXT      NOT NULL UNIQUE,
    validity_period         DATE      NOT NULL,
    status                  TEXT      NOT NULL,
    balance                 BIGINT    NOT NULL,

    PRIMARY KEY (id)
);

--changeset Alexandr-Kokorin:4
INSERT INTO global_permission(id, name)
values (1, 'USER');
INSERT INTO global_permission(id, name)
values (2, 'ADMIN');

--changeset Alexandr-Kokorin:5
INSERT INTO application_user (email, display_name, hashed_password, global_permission_id)
VALUES ('admin@gmail.com', 'Admin', '$2a$10$WFRQhlz7Ul85HsRjMg3XNutiB//3HLloe3vTuW6GDPD9eeXeYXiJe', 2);
