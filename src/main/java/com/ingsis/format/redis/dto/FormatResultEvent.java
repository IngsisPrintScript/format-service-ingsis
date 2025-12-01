package com.ingsis.format.redis.dto;

import java.util.UUID;

public record FormatResultEvent(String userId, UUID snippetId, FormatStatus status, String content) {
}
