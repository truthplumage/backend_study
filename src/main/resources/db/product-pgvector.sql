-- Run this after the application has created the `public."product"` table.
-- It converts the embedding column to pgvector, then adds a cosine-distance index.

CREATE EXTENSION IF NOT EXISTS vector;

ALTER TABLE public."product"
    ALTER COLUMN embedding TYPE vector(1536)
    USING embedding::vector(1536);

CREATE INDEX IF NOT EXISTS idx_product_embedding_cosine
    ON public."product"
    USING hnsw (embedding vector_cosine_ops);
