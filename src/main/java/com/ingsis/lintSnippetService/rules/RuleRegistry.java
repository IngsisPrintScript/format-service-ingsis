package com.ingsis.lintSnippetService.rules;

import com.ingsis.lintSnippetService.rules.rules.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

@Component
public class RuleRegistry {

  private final Map<String, FormatRule> rules = new HashMap<>();

  public RuleRegistry() {
    rules.put("LineJumpBeforePrintln", new LineJumpBeforePrintln());
    rules.put("SpacesInAssignSymbol", new SpacesInAssignSymbol());
    rules.put("spaceAfterColon",new SpaceAfterColon());
    rules.put("spaceBeforeColon",new SpaceBeforeColon());
    rules.put("indentationInsideConditionals",new IndentationInsideConditionals());
    rules.put("spaceBetweenOperator",new SpaceBetweenOperator());
    rules.put("spaceBetweenTokens",new SpaceBetweenTokens());
    rules.put("jumpAfterSemicolon",new JumpAfterSemicolon());
    rules.put("indentationSameAsIf",new IndentationSameAsIf());
    rules.put("KeyBracketAlone",new KeyBracketAlone());
  }

  public FormatRule getRule(String name) {
    try {
      return rules.get(name);
    }catch (Exception e){
      throw new NoSuchElementException("rule not found");
    }
  }
}
