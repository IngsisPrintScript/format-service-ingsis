package com.ingsis.format.rules;

public interface FormatRule {
  String getName();

  String apply(String code, String value);
}
