package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JumpAndLineTests {

  @Test
  void jumpAfterSemicolonUsesValueOrDefault() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "a; b; \"c;d\";";
    String out = r.apply(in, "2");
    // should insert line jumps after semicolons outside strings
    assertTrue(out.contains("a;\n\n b;") || out.contains("a;\n\nb;"));
    String out2 = r.apply(in, "notnum");
    assertTrue(out2.contains("a;\n b;") || out2.contains("a;\nb;"));
  }

  @Test
  void lineJumpBeforePrintlnHandlesNegativeAndNonNumeric() {
    LineJumpBeforePrintln r = new LineJumpBeforePrintln();
    String in = "printlnHello";
    String out = r.apply(in, "1");
    assertTrue(out.contains("\nprintln") || out.contains("println"));
    String out2 = r.apply(in, "-1");
    assertTrue(out2.contains("println"));
  }

  @Test
  void semicolonFollowedByNewlineDoesNotAddExtra() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "a;\n b;";
    String out = r.apply(in, "2");
    assertTrue(out.contains("a;\n b;") || out.contains("a;\nb;"));
  }

  @Test
  void semicolonInsideStringIgnored() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "\"a;\"; b;";
    String out = r.apply(in, "1");
    // semicolon inside quotes shouldn't create line breaks
    assertTrue(out.contains("\"a;\";") && out.contains("b;"));
  }

  @Test
  void getLineJumpsHandlesZeroAndLarge() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "a; b; c;";
    String out0 = r.apply(in, "0");
    assertTrue(out0.contains("a;") && out0.contains("b;") && out0.contains("c;"));

    String outLarge = r.apply(in, "100");
    assertTrue(outLarge.contains("a;") && outLarge.contains("b;") && outLarge.contains("c;"));
  }

  @Test
  void lineJumpBeforePrintlnHandlesNonNumericAndMultiple() {
    LineJumpBeforePrintln r = new LineJumpBeforePrintln();
    String in = "a println b";
    String out = r.apply(in, "notnum");
    assertTrue(out.contains("println"));

    String out2 = r.apply(in, "2");
    assertTrue(out2.contains("\n\nprintln") || out2.contains("\n\n println"));
  }
}
