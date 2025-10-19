package com.ingsis.format.rules.rules;

import com.ingsis.format.rules.FormatRule;

public class SpaceBetweenOperator implements FormatRule {
    @Override
    public String getName() {
        return "spaceBetweenOperator";
    }

    @Override
    public String apply(String content, String value) {
        String[] operators = { "\\+", "-", "\\*", "/", "%", ">", "<", "&&", "\\|\\|", "!" };
        String formatted = content;

        for (String op : operators) {
            formatted = formatted.replaceAll("\\s*" + op + "\\s*", " " + op.replace("\\", "") + " ");
        }

        formatted = formatted.replaceAll("\\s+", " ").trim();

        return formatted;
    }
}
