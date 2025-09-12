-- Основная таблица пользователей
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     first_name VARCHAR(255) NOT NULL,
                                     last_name VARCHAR(255),
                                     profile_picture_url VARCHAR(512),
                                     job_title VARCHAR(255),
                                     tech_stack VARCHAR(255),
                                     seniority VARCHAR(50),
                                     resume_id BIGINT,
                                     company_id BIGINT,
                                     created_at TIMESTAMP NOT NULL
);


CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role VARCHAR(100) NOT NULL,
                                          CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

insert into users values (1, 'user1@mail.com', 'user1', 'mar', 'NONE', 'Java Developer', 'Java, Spring Boot, SQL', 'SENIOR', '1', '1', '2025-08-01 14:16:06.191342');
insert into user_roles values (1, 'ROLE_CANDIDATE');

insert into users values (2, 'user2@mail.com', 'user2', 'mar', 'NONE', 'React Developer', 'React, TypeScript', 'SENIOR', '2', '2', '2025-08-01 14:16:06.191342');
insert into user_roles values (2, 'ROLE_CANDIDATE');

insert into users values (3, 'rec1@mail.com', 'rec1', 'mar', 'NONE', 'Recruiter', 'Recruitment', 'SENIOR', '3', '3', '2025-08-01 14:16:06.191342');
insert into user_roles values (3, 'ROLE_RECRUITER');

insert into users values (4, 'rec2@mail.com', 'rec2', 'mar', 'NONE', 'Recruiter', 'Recruitment', 'SENIOR', '4', '4', '2025-08-01 14:16:06.191342');
insert into user_roles values (4, 'ROLE_RECRUITER');
