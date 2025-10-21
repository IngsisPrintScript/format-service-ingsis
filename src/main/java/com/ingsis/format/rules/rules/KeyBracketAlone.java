package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class KeyBracketAlone implements FormatRule {
  @Override
  public String getName() {
    return "keyBracketAlone";
  }

  @Override
  public String apply(String content, String value) {
    String formatted = content.replaceAll("\\s*\\{", "\n{");
    return formatted.trim();
  }
}
