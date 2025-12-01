package com.ingsis.format.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetFormatRuleTest {

    @Test
    void recordContainsValues() {
        UUID id = UUID.randomUUID();
        GetFormatRule r = new GetFormatRule(id, "n", false);
        assertEquals(id, r.id());
        assertEquals("n", r.name());
        assertFalse(r.active());
    }
}
