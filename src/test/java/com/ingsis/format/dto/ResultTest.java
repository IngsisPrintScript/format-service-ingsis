package com.ingsis.format.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResultTest {

  @Test
  void recordContent() {
    Result r = new Result("ok");
    assertEquals("ok", r.content());
  }
}
