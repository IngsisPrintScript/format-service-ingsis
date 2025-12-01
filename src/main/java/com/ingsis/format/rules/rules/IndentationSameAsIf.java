package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class IndentationSameAsIf implements FormatRule {
    @Override
    public String getName() {
        return "indentationSameAsIf";
    }

    @Override
    public String apply(String content, String value) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.matches("^if\\s*\\(.*\\).*")) {
                if (!trimmed.contains("{")) {
                    trimmed += " {";
                } else {
                    trimmed = trimmed.replaceAll("\\)\\s*\\{", ") {");
                }
            }

            result.append(trimmed).append("\n");
        }

        return result.toString().trim();
    }
}
