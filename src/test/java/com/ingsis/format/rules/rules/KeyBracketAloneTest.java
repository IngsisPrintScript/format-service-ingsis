package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KeyBracketAloneTest {

  @Test
  void movesBraceToNewLine() {
    KeyBracketAlone r = new KeyBracketAlone();
    String in = "function {";
    String out = r.apply(in, null);
    assertTrue(out.startsWith("function") && out.contains("\n{"));
  }

  @Test
  void noBraceLeavesContent() {
    KeyBracketAlone r = new KeyBracketAlone();
    String in = "noBraceHere";
    String out = r.apply(in, null);
    assertEquals("noBraceHere", out);
  }

  @Test
  void multipleBracesAndSpaces() {
    KeyBracketAlone r = new KeyBracketAlone();
    String in = "func { inner{";
    String out = r.apply(in, null);
    // braces should be moved so they appear on their own line
    assertTrue(out.contains("\n{") && out.contains("inner"));
  }

  @Test
  void getNameReturnsExpected() {
    KeyBracketAlone r = new KeyBracketAlone();
    assertEquals("keyBracketAlone", r.getName());
  }
}
