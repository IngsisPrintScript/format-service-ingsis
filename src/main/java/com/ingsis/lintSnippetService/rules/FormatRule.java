package com.ingsis.lintSnippetService.rules;

public interface FormatRule {
  String getName();
  String apply(String code, String value);
}
