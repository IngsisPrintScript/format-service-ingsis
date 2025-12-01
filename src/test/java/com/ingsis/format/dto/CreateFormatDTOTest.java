package com.ingsis.format.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CreateFormatDTOTest {

    @Test
    void recordProperties() {
        CreateFormatDTO dto = new CreateFormatDTO("n", "v", true);
        assertEquals("n", dto.name());
        assertEquals("v", dto.defaultValue());
        assertTrue(dto.active());
    }
}
