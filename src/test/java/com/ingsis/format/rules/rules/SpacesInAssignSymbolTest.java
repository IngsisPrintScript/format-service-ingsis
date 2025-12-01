package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpacesInAssignSymbolTest {

  @Test
  void addsSpacesAroundEqualsOutsideStrings() {
    SpacesInAssignSymbol r = new SpacesInAssignSymbol();
    String in = "a= b\"c=d\" e=f";
    String out = r.apply(in, null);
    assertTrue(out.contains("a = b") || out.contains("a = b\""));
    assertTrue(out.contains("e = f"));
  }
}
