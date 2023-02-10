-- USERS
INSERT INTO users (id, active, country, creation_date, delete_date, email, end_plan, failed_retries, image_uid, last_connection, last_name, name, password, plan, sector, start_plan, username) VALUES(1, 1, 'ES', '2023-01-30 10:13:47.355', NULL, 'danaremar@ev.us.es', NULL, 0, 'cute-astronaut-1.jpg', '2023-01-30 10:13:47.355', 'Arellano', 'Daniel', '$2a$06$BgaA68fT5BJZtzPCh6CY1.GecHcyhZA9DvA7TxoC0kXwZfe3wF/p2', 'FREE', 'Software', NULL, 'danaremar');
INSERT INTO users (id, active, country, creation_date, delete_date, email, end_plan, failed_retries, image_uid, last_connection, last_name, name, password, plan, sector, start_plan, username) VALUES(2, 1, 'ES', '2023-01-30 10:13:47.355', NULL, 'premium@ev.us.es', '2028-01-30 10:13:47.355', 0, 'cute-astronaut-1.jpg', '2023-01-30 10:13:47.355', 'Arellano', 'Daniel', '$2a$06$BgaA68fT5BJZtzPCh6CY1.GecHcyhZA9DvA7TxoC0kXwZfe3wF/p2', 'PREMIUM', 'Software', '2023-01-30 10:13:47.355', 'premium');

-- PROJECTS
INSERT INTO projects(id, active, creation_date, close_date, description, name) VALUES(1, 1, '2021-01-31 12:25:01', NULL, 'Incredible project', 'IMAN PROJECT');

-- PROJECT ROLES
INSERT INTO project2role(id, accepted, `role`, project_id, user_id) VALUES(1, 1, 0, 1, 1);