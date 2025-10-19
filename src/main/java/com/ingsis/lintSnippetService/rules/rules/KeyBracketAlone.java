package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

public class KeyBracketAlone implements FormatRule {
    @Override
    public String getName() {
        return "keyBracketAlone";
    }

    @Override
    public String apply(String code, String value) {
        while (code.matches(".*\\{[^\\{]*\\bif\\b.*")) {
            code = code.replaceFirst("\\{(?=[^\\{]*\\bif\\b)", "");
        }
        return code;    }
}
