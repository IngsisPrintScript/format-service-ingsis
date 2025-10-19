package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class JumpAfterSemicolon implements FormatRule {
    @Override
    public String getName() {
        return "jumpAfterSemicolon";
    }

    @Override
    public String apply(String code, String value) {
        StringBuilder formatted = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '"') {
                inString = !inString;
                formatted.append(c);
                continue;
            }
            if (c == ';' && !inString) {
                formatted.append(';');
                if (i + 1 < code.length() && code.charAt(i + 1) != '\n') {
                    formatted.append('\n');
                }
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString().trim();
    }
}
