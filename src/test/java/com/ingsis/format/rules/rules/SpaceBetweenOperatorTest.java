package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpaceBetweenOperatorTest {

  @Test
  void addsSpacesAroundOperators() {
    SpaceBetweenOperator r = new SpaceBetweenOperator();
    String in = "a+b*c -d/ e%f>g&&h||i!j";
    String out = r.apply(in, null);
    assertTrue(out.contains("a + b"));
    assertTrue(out.contains("c - d"));
    assertTrue(out.contains("d / e"));
    assertTrue(out.contains("e % f"));
  }
}
