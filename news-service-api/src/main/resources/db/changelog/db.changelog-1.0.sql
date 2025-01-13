--liquibase formatted sql

--changeset ryabchikov:1
CREATE TABLE news
(
    id    UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    title VARCHAR(255)             NOT NULL,
    text  TEXT                     NOT NULL,
    time  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);