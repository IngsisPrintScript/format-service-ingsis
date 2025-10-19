package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class IndentationInsideConditionals implements FormatRule {
    @Override
    public String getName() {
        return "indentationInsideConditionals";
    }

    @Override
    public String apply(String code, String value) {
        int indentSpaces = 1;
        try {
            indentSpaces = Integer.parseInt(value);
        } catch (NumberFormatException ignored) {

        }

        String line = code.trim();

        if (line.startsWith("}")) {
            return line;
        }

        return " ".repeat(Math.max(0, indentSpaces)) +
                line;
    }
}
