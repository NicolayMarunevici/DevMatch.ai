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