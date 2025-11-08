CREATE TABLE IF NOT EXISTS outbox_events (
                                             id          UUID PRIMARY KEY,
                                             topic       VARCHAR(255) NOT NULL,
                                             event_key   VARCHAR(255) NOT NULL,
                                             payload     JSONB        NOT NULL,
                                             created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                                             published   BOOLEAN      NOT NULL DEFAULT FALSE,
                                             attempts    INT          NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_outbox_unpublished_created
    ON outbox_events (published, created_at, id);

CREATE INDEX IF NOT EXISTS idx_outbox_event_key
    ON outbox_events (event_key);