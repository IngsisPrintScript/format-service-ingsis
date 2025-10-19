package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

import java.util.Set;

public class SpaceBeforeColon implements FormatRule {
    @Override
    public String getName() {
        return "spaceBeforeColon";
    }

    @Override
    public String apply(String code, String value) {
        StringBuilder sb = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            if (c == '"') {
                inString = !inString;
            }

            if (c == ':' && !inString) {
                if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == ' ') {
                    sb.setCharAt(sb.length() - 1, ' ');
                } else {
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
