package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class SpacesInAssignSymbol implements FormatRule {
    @Override
    public String getName() {
        return "readInputNoExpressionArgumentsRule";
    }

    @Override
    public String apply(String content, String value) {
        StringBuilder formatted = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '"') {
                inString = !inString;
                formatted.append(c);
                continue;
            }

            if (!inString && c == '=') {
                if (!formatted.isEmpty() && formatted.charAt(formatted.length() - 1) != ' ') {
                    formatted.append(' ');
                }
                formatted.append('=');
                if (i + 1 < content.length() && content.charAt(i + 1) != ' ') {
                    formatted.append(' ');
                }
            } else {
                formatted.append(c);
            }
        }
        return formatted.toString().replaceAll("\\s+", " ").trim();
    }
}
