CREATE TABLE IF NOT EXISTS gallery.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS gallery.user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES gallery.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_users_username ON gallery.users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON gallery.users(email);
CREATE INDEX IF NOT EXISTS idx_users_enabled ON gallery.users(enabled);