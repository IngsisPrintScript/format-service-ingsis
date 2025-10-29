package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class SpaceBeforeColon implements FormatRule {
  @Override
  public String getName() {
    return "spaceBeforeColon";
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
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != ' ') {
          sb.append(' ');
        }
        sb.append(':');
      } else {
        sb.append(c);
      }
    }

    return sb.toString();
  }
}
