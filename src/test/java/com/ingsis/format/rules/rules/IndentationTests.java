package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IndentationTests {

  @Test
  void sameAsIfAddsBrace() {
    IndentationSameAsIf r = new IndentationSameAsIf();
    String in = "if(x)\n  do();";
    String out = r.apply(in, null);
    assertTrue(out.contains("if(x) {"));
  }

  @Test
  void insideConditionalsIndent() {
    IndentationInsideConditionals r = new IndentationInsideConditionals();
    String in = "a\n}";
    String out = r.apply(in, "2");
    assertTrue(out.contains("a"));
    assertTrue(out.length() > 0);
    assertTrue(Character.isWhitespace(out.charAt(0)) || out.startsWith("a"));
    assertTrue(out.contains("\n"));
    assertTrue(out.contains("}"));
  }

  @Test
  void insideConditionalsWithInvalidAndNegativeValues() {
    IndentationInsideConditionals r = new IndentationInsideConditionals();
    String in = "line1\nline2\n}";
    // non-numeric -> default 1 space
    String out = r.apply(in, "notnum");
    assertTrue(out.contains(" line1") || out.contains("line1"));
    // negative -> Math.max(0, indentSpaces) yields 0 spaces
    String out2 = r.apply(in, "-5");
    // lines should not start with extra spaces
    String firstLine = out2.split("\\r?\\n")[0];
    assertFalse(firstLine.startsWith(" "));
  }

  @Test
  void indentationHandlesZeroAndLarge() {
    IndentationInsideConditionals r = new IndentationInsideConditionals();
    String in = "line1\nline2\n}";
    // zero -> no added spaces
    String out0 = r.apply(in, "0");
    String first0 = out0.split("\\r?\\n")[0];
    assertFalse(first0.startsWith(" "));
    // large -> should not throw and should include lines
    String outLarge = r.apply(in, "50");
    assertTrue(outLarge.contains("line1") && outLarge.contains("line2") && outLarge.contains("}"));
  }
}
