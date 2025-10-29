package com.ingsis.format.dto;

import com.ingsis.format.rules.FormatRule;

import java.util.UUID;

public record GetFormatRule(UUID id, String name, boolean active){
}
