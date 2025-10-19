package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class IndentationSameAsIf implements FormatRule {
    @Override
    public String getName() {
        return "indentationSameAsIf";
    }

    @Override
    public String apply(String code, String value) {
        String line = code.trim();
        if (line.matches("^if\\s*\\(.*\\).*")) {
            if (!line.contains("{")) {
                line += " {";
            } else {
                line = line.replaceAll("\\)\\s*\\{", ") {");
            }
        }
        return line;
    }
}
