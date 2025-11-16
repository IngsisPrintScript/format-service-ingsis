package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpaceBetweenTokensTest {

  @Test
  void formatsPunctuationAndSpaces() {
    SpaceBetweenTokens rule = new SpaceBetweenTokens();
    String input = "a ,b;  ( c )";
    String out = rule.apply(input, null);
    assertEquals("a, b; (c)", out);
  }
}
