CREATE TABLE friend
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    user_id               VARCHAR(255) NOT NULL,
    friend_id             VARCHAR(255) NOT NULL,
    friend_status         VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT pk_friend PRIMARY KEY (id)
);

CREATE TABLE friend_request
(
    id         VARCHAR(255) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    receiver_id               VARCHAR(255) NOT NULL,
    requester_id             VARCHAR(255) NOT NULL,
    request_status VARCHAR(255) NOT NULL,
    friend_request_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT pk_friend_request PRIMARY KEY (id)
);