
CREATE TABLE user
(
    id                 VARCHAR(255) NOT NULL,
    created_at         datetime     NULL,
    updated_at         datetime     NULL,
    nickname           VARCHAR(255) NOT NULL,
    email              VARCHAR(100) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);


CREATE TABLE user_auth
(
    id                    VARCHAR(255) NOT NULL,
    created_at            datetime     NULL,
    updated_at            datetime     NULL,
    user_id               VARCHAR(255) NOT NULL,
    social_id             VARCHAR(255) NULL,
    login_provider VARCHAR(255) NOT NULL,
    password_hash          VARCHAR(255) NULL,

    CONSTRAINT pk_user_auth PRIMARY KEY (id)
);

CREATE INDEX idx_social_id_index ON user_auth (social_id);

ALTER TABLE user_auth
    ADD CONSTRAINT FK_USER_AUTH_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id);




CREATE TABLE user_token
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    token      VARCHAR(500) NOT NULL,
    user_id    VARCHAR(500) NULL,
    CONSTRAINT pk_user_token PRIMARY KEY (id)
);

ALTER TABLE user_token
    ADD CONSTRAINT uc_user_token_token UNIQUE (token);

CREATE UNIQUE INDEX uk_token_index ON user_token (token);