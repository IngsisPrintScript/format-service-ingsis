package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class LineJumpBeforePrintln implements FormatRule {
  @Override
  public String getName() {
    return "lineJumpBeforePrintln";
  }

  @Override
  public String apply(String content, String value) {
    int lineJumps = 0;
    try {
      lineJumps = Integer.parseInt(value);
      if (lineJumps < 0) lineJumps = 0;
    } catch (NumberFormatException ignored) {}

    return content.replaceAll(
            "(?<!\\n)println",
            "\n".repeat(lineJumps) + "println"
    ).trim();
  }
}
