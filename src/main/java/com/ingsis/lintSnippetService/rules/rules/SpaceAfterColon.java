package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.FormatRule;

import javax.print.DocFlavor;
import java.util.Set;

public class SpaceAfterColon implements FormatRule {

    @Override
    public String getName() {
        return "spaceAfterColon";
    }

    @Override
    public String apply(String code, String value) {
        StringBuilder sb = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            if (c == '"') {
                inString = !inString;
            }

            if (c == ':' && !inString) {
                sb.append(':');
                if (i + 1 < code.length() && code.charAt(i + 1) != ' ') {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
