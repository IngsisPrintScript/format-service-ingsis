package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class SpaceBetweenOperator implements FormatRule {
    @Override
    public String getName() {
        return "spaceBetweenOperator";
    }

    @Override
    public String apply(String code, String value) {
        StringBuilder formatted = new StringBuilder();
        String line = code;
        String[] operators = { "\\+", "-", "\\*", "/", "%", ">", "<", "&&", "\\|\\|", "!" };
        for (String op : operators) {
            line = line.replaceAll("\\s*" + op + "\\s*", " " + op.replace("\\","") + " ");
        }
        line = line.replaceAll("\\s+", " ").trim();
        formatted.append(line);
        return formatted.toString().trim();
    }
}
