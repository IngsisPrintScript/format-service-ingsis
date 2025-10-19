package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class SpaceBetweenTokens implements FormatRule {
    @Override
    public String getName() {
        return "spaceBetweenTokens";
    }

    @Override
    public String apply(String code, String value) {
        String line = code.trim();
        StringBuilder newLine = new StringBuilder();
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == ':' || c == '=') {
                newLine.append(c);
            } else if (Character.isWhitespace(c)) {
                if (i > 0 && newLine.charAt(newLine.length() - 1) != ' ') {
                    newLine.append(' ');
                }
            } else {
                newLine.append(c);
            }
        }
        return  newLine.toString().trim();
    }
}
