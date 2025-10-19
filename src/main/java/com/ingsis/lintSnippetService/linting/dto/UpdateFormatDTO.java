package com.ingsis.lintSnippetService.linting.dto;

import java.util.UUID;

public record UpdateFormatDTO(UUID lintId, String value, boolean active) {}
