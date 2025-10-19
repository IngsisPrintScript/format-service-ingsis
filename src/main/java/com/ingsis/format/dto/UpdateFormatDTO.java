package com.ingsis.format.dto;

import java.util.UUID;

public record UpdateFormatDTO(UUID formatId, String value, boolean active) {}
