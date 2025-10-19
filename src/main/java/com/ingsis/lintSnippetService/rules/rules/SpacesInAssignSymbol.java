package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class SpacesInAssignSymbol implements FormatRule {
  @Override
  public String getName() {
    return "readInputNoExpressionArgumentsRule";
  }

  @Override
  public String apply(String code, String value) {
    StringBuilder formatted = new StringBuilder();
    String line = code;
    if (line.contains("=")) {
      line = line
              .replaceAll("\\s*=\\s*", " = ")
              .replaceAll("\\s+", " ")
              .trim();
    }
    formatted.append(line);
    return formatted.toString().trim();
  }
}
