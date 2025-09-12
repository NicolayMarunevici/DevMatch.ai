CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255),
                       provider VARCHAR(50),
                       enabled BOOLEAN,
                       locked BOOLEAN
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles(name) VALUES ('ROLE_CANDIDATE'), ('ROLE_RECRUITER'), ('ROLE_ADMIN');

INSERT into users values (1, 'user1', 'mar', 'user1@mail.com', '$2a$12$YWqdvUbjmz8WFZFtgV.4ruCn4dfbgoo397YPjL7xIO/4zbccJEcJK', 'NONE', true, false);
insert into user_roles values (1, 1);

INSERT into users values (2, 'user2', 'mar', 'user2@mail.com', '$2a$12$YWqdvUbjmz8WFZFtgV.4ruCn4dfbgoo397YPjL7xIO/4zbccJEcJK', 'NONE', true, false);
insert into user_roles values (2, 1);

INSERT into users values (3, 'rec1', 'mar', 'rec1@mail.com', '$2a$12$YWqdvUbjmz8WFZFtgV.4ruCn4dfbgoo397YPjL7xIO/4zbccJEcJK', 'NONE', true, false);
insert into user_roles values (3, 2);

INSERT into users values (4, 'rec2', 'mar', 'rec2@mail.com', '$2a$12$YWqdvUbjmz8WFZFtgV.4ruCn4dfbgoo397YPjL7xIO/4zbccJEcJK', 'NONE', true, false);
insert into user_roles values (4, 2);



-- INSERT INTO user_roles values (1, 1);