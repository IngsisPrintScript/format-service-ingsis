package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class JumpAfterSemicolon implements FormatRule {
    @Override
    public String getName() {
        return "jumpAfterSemicolon";
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

            if (c == ';' && !inString) {
                formatted.append(';');

                if (i + 1 < content.length() && content.charAt(i + 1) != '\n') {
                    formatted.append("\n".repeat(getLineJumps(value)));
                }
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString().trim();
    }

    private int getLineJumps(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
