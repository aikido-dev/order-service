CREATE TABLE users
(
    id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username  TEXT NOT NULL,
    password  TEXT NOT NULL,
    role      TEXT NOT NULL DEFAULT 'USER',

    CONSTRAINT uq_username UNIQUE (username),
    CONSTRAINT chk_user_role CHECK (users.role IN ('USER', 'ADMIN'))
);

CREATE TABLE orders
(
    id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id  UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE ,
    description  TEXT NOT NULL,
    status      TEXT NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT chk_order_status CHECK (orders.status IN ('CREATED', 'IN_PROGRESS', 'COMPLETED'))
);