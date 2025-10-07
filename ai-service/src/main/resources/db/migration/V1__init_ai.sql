CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS vector;

-- эмбеддинги
CREATE TABLE IF NOT EXISTS embeddings (
                                          id UUID PRIMARY KEY,
                                          entity_type TEXT NOT NULL,     -- USER | VACANCY
                                          entity_id   TEXT NOT NULL,
                                          model       TEXT NOT NULL,
                                          dim         INT  NOT NULL,
                                          vector      vector(1024) NOT NULL, -- подставь реальную размерность модели
                                          created_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_embeddings_entity ON embeddings(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_embeddings_ivf ON embeddings USING ivfflat (vector vector_cosine_ops) WITH (lists = 100);

-- объяснения
CREATE TABLE IF NOT EXISTS explanations (
                                            id UUID PRIMARY KEY,
                                            user_id    TEXT NOT NULL,
                                            vacancy_id TEXT NOT NULL,
                                            summary    TEXT NOT NULL,
                                            bullets    jsonb NOT NULL,
                                            created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS rag_chunks (
                                          id         TEXT PRIMARY KEY,              -- "u:<userId>:<n>" / "v:<vacancyId>:<n>" / "d:<docId>:<n>"
                                          owner_type TEXT NOT NULL,                 -- USER | VACANCY | DOC
                                          owner_id   TEXT NOT NULL,
                                          model      TEXT NOT NULL,
                                          dim        INT  NOT NULL,
                                          vector     vector(1024) NOT NULL,         -- ВАЖНО: подгони под размер модели эмбеддингов
                                          text       TEXT NOT NULL,
                                          meta       jsonb NOT NULL DEFAULT '{}'::jsonb,
                                          created_at timestamptz NOT NULL DEFAULT now()
);

-- ANN индекс под cosine
CREATE INDEX IF NOT EXISTS idx_rag_chunks_ivf
    ON rag_chunks USING ivfflat (vector vector_cosine_ops) WITH (lists = 150);

-- Индекс для фильтрации по владельцу
CREATE INDEX IF NOT EXISTS idx_rag_chunks_owner ON rag_chunks(owner_type, owner_id);

-- Полнотекстовый индекс (простой словарь)
CREATE INDEX IF NOT EXISTS idx_rag_chunks_fts
    ON rag_chunks USING GIN (to_tsvector('simple', text));