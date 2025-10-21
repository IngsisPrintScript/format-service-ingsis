package com.ingsis.format.rules;

import com.ingsis.format.rules.rules.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;

@Component
public class RuleRegistry {

  private final Map<String, FormatRule> rules = new HashMap<>();

  public RuleRegistry() {
    rules.put("lineJumpBeforePrintln", new LineJumpBeforePrintln());
    rules.put("spacesInAssignSymbol", new SpacesInAssignSymbol());
    rules.put("spaceAfterColon", new SpaceAfterColon());
    rules.put("spaceBeforeColon", new SpaceBeforeColon());
    rules.put("indentationInsideConditionals", new IndentationInsideConditionals());
    rules.put("spaceBetweenOperator", new SpaceBetweenOperator());
    rules.put("spaceBetweenTokens", new SpaceBetweenTokens());
    rules.put("jumpAfterSemicolon", new JumpAfterSemicolon());
    rules.put("indentationSameAsIf", new IndentationSameAsIf());
    rules.put("keyBracketAlone", new KeyBracketAlone());
  }

  public FormatRule getRule(String name) {
    try {
      return rules.get(name);
    } catch (Exception e) {
      throw new NoSuchElementException("rule not found");
    }
  }
}
