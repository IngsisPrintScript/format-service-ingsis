package com.ingsis.format.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.ingsis.format.rules.FormatRule;
import org.junit.jupiter.api.Test;

class AllRulesGetNameTest {

  @Test
  void allRulesExposeName() {
    java.util.List<FormatRule> rules =
        java.util.List.of(
            new LineJumpBeforePrintln(),
            new SpacesInAssignSymbol(),
            new SpaceAfterColon(),
            new SpaceBeforeColon(),
            new IndentationInsideConditionals(),
            new SpaceBetweenOperator(),
            new SpaceBetweenTokens(),
            new JumpAfterSemicolon(),
            new IndentationSameAsIf(),
            new KeyBracketAlone());

    for (FormatRule r : rules) {
      assertNotNull(r.getName());
      assertFalse(r.getName().isBlank());
    }
  }

  @Test
  void reflectivelyInvokeGetName() throws Exception {
    java.lang.reflect.Method m = LineJumpBeforePrintln.class.getMethod("getName");
    Object res = m.invoke(new LineJumpBeforePrintln());
    assertEquals("lineJumpBeforePrintln", res);
  }

  @Test
  void reflectivelyInvokeJumpAfterSemicolon_getLineJumps_parsesNumber() throws Exception {
    java.lang.reflect.Method m =
        JumpAfterSemicolon.class.getDeclaredMethod("getLineJumps", String.class);
    m.setAccessible(true);
    Object res = m.invoke(new JumpAfterSemicolon(), "3");
    assertEquals(3, res);
  }

  @Test
  void reflectivelyInvokeJumpAfterSemicolon_getLineJumps_handlesNonNumeric() throws Exception {
    java.lang.reflect.Method m =
        JumpAfterSemicolon.class.getDeclaredMethod("getLineJumps", String.class);
    m.setAccessible(true);
    Object res = m.invoke(new JumpAfterSemicolon(), "not-a-number");
    assertEquals(1, res);
  }

  @Test
  void spacesInAssignSymbol_getName_returnsExpected() {
    SpacesInAssignSymbol r = new SpacesInAssignSymbol();
    assertEquals("readInputNoExpressionArgumentsRule", r.getName());
  }

  @Test
  void jumpAfterSemicolon_apply_addsMultipleLineBreaks_forNumericValue() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "first;second";
    String out = r.apply(in, "2");
    // expect a semicolon followed by two newlines between tokens
    assertTrue(out.contains(";\n\n"));
    assertTrue(out.startsWith("first"));
    assertTrue(out.endsWith("second"));
  }

  @Test
  void jumpAfterSemicolon_apply_ignoresSemicolonsInsideStrings() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "println(\"a;\");b;";
    String out = r.apply(in, "1");
    assertTrue(out.contains("\"a;\"") && out.contains("b;"));
    assertFalse(out.contains("\"a;\n"));
  }

  @Test
  void jumpAfterSemicolon_apply_preservesExistingNewline() {
    JumpAfterSemicolon r = new JumpAfterSemicolon();
    String in = "one;\nnext";
    String out = r.apply(in, "2");
    assertTrue(out.contains(";\nnext"));
    assertFalse(out.contains(";\n\n"));
  }
}
