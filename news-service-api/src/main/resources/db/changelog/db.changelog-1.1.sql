--liquibase formatted sql

--changeset ryabchikov:1
ALTER TABLE news ADD COLUMN author_id UUID NOT NULL;