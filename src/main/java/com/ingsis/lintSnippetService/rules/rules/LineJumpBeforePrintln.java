package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class LineJumpBeforePrintln implements FormatRule {
  @Override
  public String getName() {
    return "lineJumpBeforePrintln";
  }

  @Override
  public String apply(String code, String value) {
    int lineJumps = 0;
    try {
      lineJumps = Integer.parseInt(value);
      if (lineJumps < 0) lineJumps = 0;
    } catch (NumberFormatException ignored) {}
    return "\n".repeat(lineJumps) + code;
  }
}
