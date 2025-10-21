package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class SpaceBetweenTokens implements FormatRule {
  @Override
  public String getName() {
    return "spaceBetweenTokens";
  }

  @Override
  public String apply(String content, String value) {
    String formatted = content.replaceAll("\\s+", " ");

    formatted = formatted.replaceAll("\\s*([;,()])\\s*", "$1");

    formatted = formatted.replaceAll("([;,])([^\\s])", "$1 $2");

    return formatted.trim();
  }
}
