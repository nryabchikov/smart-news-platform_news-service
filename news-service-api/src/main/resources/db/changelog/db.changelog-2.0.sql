--liquibase formatted sql

--changeset ryabchikov:1
INSERT INTO news (id, title, text, time, author_id)
VALUES ('e081a618-7a02-4e81-852d-4caf3debbc20', 'News Title 1', 'This is the content of news 1.', NOW(), gen_random_uuid()),
       ('382ba352-2e36-4de5-b9de-0a5820cb4ed7', 'News Title 2', 'This is the content of news 2.', NOW(), gen_random_uuid()),
       ('9a54d9ac-82a4-40cb-9c2f-ff6f53dab26d', 'News Title 3', 'This is the content of news 3.', NOW(), gen_random_uuid()),
       ('e92a06d1-5103-4e0d-a001-a8a6febf22b8', 'News Title 4', 'This is the content of news 4.', NOW(), gen_random_uuid()),
       ('ce68d7ed-e750-48a5-bb3c-3a1942957b40', 'News Title 5', 'This is the content of news 5.', NOW(), gen_random_uuid()),
       ('273c5355-0962-424b-b4e8-203782fd3350', 'News Title 6', 'This is the content of news 6.', NOW(), gen_random_uuid()),
       ('298f8e3b-ed3a-4221-8694-d589cad3d026', 'News Title 7', 'This is the content of news 7.', NOW(), gen_random_uuid()),
       ('ce073750-749d-413b-873d-ad71c0c44bfe', 'News Title 8', 'This is the content of news 8.', NOW(), gen_random_uuid()),
       ('7ad3545c-7986-4091-8674-c0b1cdb29b40', 'News Title 9', 'This is the content of news 9.', NOW(), gen_random_uuid()),
       ('3c9fdb7a-2c2e-4b07-807a-9341eb647593', 'News Title 10', 'This is the content of news 10.', NOW(), gen_random_uuid()),
       ('eb31fd67-fdaa-4d19-b999-bac2e65218a1', 'News Title 11', 'This is the content of news 11.', NOW(), gen_random_uuid()),
       ('c054c336-c7d6-41dc-a22a-04a8fbb51b10', 'News Title 12', 'This is the content of news 12.', NOW(), gen_random_uuid()),
       ('cdb4b811-3100-4f6f-963a-0a94f8a23135', 'News Title 13', 'This is the content of news 13.', NOW(), gen_random_uuid()),
       ('57eb3852-4776-4049-b2c4-6a76dbbc73fd', 'News Title 14', 'This is the content of news 14.', NOW(), gen_random_uuid()),
       ('fd0751e0-7826-412c-addc-abedef7dc18b', 'News Title 15', 'This is the content of news 15.', NOW(), gen_random_uuid()),
       ('0608d3d6-93f3-4f5a-8187-fccb649f9618', 'News Title 16', 'This is the content of news 16.', NOW(), gen_random_uuid()),
       ('c346adb9-f61a-4852-a261-420878696613', 'News Title 17', 'This is the content of news 17.', NOW(), gen_random_uuid()),
       ('0fded22b-a932-4940-9416-00834e2d5c7e', 'News Title 18', 'This is the content of news 18.', NOW(), gen_random_uuid()),
       ('a12e2a15-ec6e-46e6-8ba4-889c5d3ed275', 'News Title 19', 'This is the content of news 19.', NOW(), gen_random_uuid()),
       ('b336af0c-2ade-4a8f-8622-f2a09a97a7b3', 'News Title 20', 'This is the content of news 20.', NOW(), gen_random_uuid());