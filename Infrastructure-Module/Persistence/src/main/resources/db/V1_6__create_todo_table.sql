CREATE TABLE todo
(
    id          VARCHAR(255) NOT NULL,
    created_at  datetime     NULL,
    updated_at  datetime     NULL,
    user_id               VARCHAR(255) NOT NULL,
    tag_id                VARCHAR(255) NOT NULL,
    raw VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    is_important BOOLEAN NOT NULL,
    is_life BOOLEAN NOT NULL,
    difficulty      INT           NOT NULL,
    estimated_time  INT           NOT NULL,
    deadline        DATE          NOT NULL,
    is_completed BOOLEAN NOT NULL,
    todo_status      VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT pk_todo PRIMARY KEY (id)
);