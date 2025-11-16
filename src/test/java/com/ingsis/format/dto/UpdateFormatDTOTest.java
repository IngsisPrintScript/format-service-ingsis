package com.ingsis.format.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateFormatDTOTest {

  @Test
  void recordValues() {
    UUID id = UUID.randomUUID();
    UpdateFormatDTO u = new UpdateFormatDTO(id, "v", true);
    assertEquals(id, u.formatId());
    assertEquals("v", u.value());
    assertTrue(u.active());
  }
}
