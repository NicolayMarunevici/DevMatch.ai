package com.devmatch.ai.domain;

import java.util.UUID;

public record Embedding(UUID id, String entityType, String entityId, String model, int dim, float[] vector) {}
