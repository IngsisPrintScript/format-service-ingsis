package com.ingsis.format.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FormatSnippetTest {

    @Test
    void recordHoldsData() {
        FormatSnippet s = new FormatSnippet("c", "owner");
        assertEquals("c", s.content());
        assertEquals("owner", s.ownerId());
    }
}
