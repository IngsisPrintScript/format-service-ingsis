package com.ingsis.format.dto;

import java.util.UUID;

public record GetFormatRule(UUID id, String name, boolean active) {
}
