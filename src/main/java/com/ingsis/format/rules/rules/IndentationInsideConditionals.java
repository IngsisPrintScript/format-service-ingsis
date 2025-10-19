package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class IndentationInsideConditionals implements FormatRule {
    @Override
    public String getName() {
        return "indentationInsideConditionals";
    }

    @Override
    public String apply(String content, String value) {
        int indentSpaces = 1;
        try {
            indentSpaces = Integer.parseInt(value);
        } catch (NumberFormatException ignored) { }

        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.startsWith("}")) {
                result.append(trimmed).append("\n");
                continue;
            }

            result.append(" ".repeat(Math.max(0, indentSpaces))).append(trimmed).append("\n");
        }

        return result.toString().trim();
    }
}
