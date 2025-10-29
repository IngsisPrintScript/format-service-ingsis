package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class SpaceAfterColon implements FormatRule {

  @Override
  public String getName() {
    return "spaceAfterColon";
  }

  @Override
  public String apply(String content, String value) {
    StringBuilder sb = new StringBuilder();
    boolean inString = false;

    for (int i = 0; i < content.length(); i++) {
      char c = content.charAt(i);

      if (c == '"') {
        inString = !inString;
      }

      if (c == ':' && !inString) {
        sb.append(':');
        if (i + 1 < content.length() && content.charAt(i + 1) != ' ') {
          sb.append(' ');
        }
      } else {
        sb.append(c);
      }
    }

    return sb.toString();
  }
}
