package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpaceBeforeAfterColonTests {

  @Test
  void beforeColonAddsSpaceOutsideStrings() {
    SpaceBeforeColon r = new SpaceBeforeColon();
    String in = "key:value \"a:b\"";
    String out = r.apply(in, null);
    assertTrue(out.contains("key :value"));
    assertTrue(out.contains("\"a:b\""));
  }

  @Test
  void afterColonAddsSpaceOutsideStrings() {
    SpaceAfterColon r = new SpaceAfterColon();
    String in = "a:b \"c:d\"";
    String out = r.apply(in, null);
    assertTrue(out.contains("a: b"));
    assertTrue(out.contains("\"c:d\""));
  }
}
