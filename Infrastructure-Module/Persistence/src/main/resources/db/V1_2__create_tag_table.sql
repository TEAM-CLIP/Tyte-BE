CREATE TABLE tag
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    user_id               VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    color      VARCHAR(255) NOT NULL,

    CONSTRAINT pk_tag PRIMARY KEY (id)
);

ALTER TABLE tag
    ADD CONSTRAINT FK_TAG_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id);