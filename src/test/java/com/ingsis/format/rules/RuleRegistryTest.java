package com.ingsis.format.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RuleRegistryTest {

  private RuleRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new RuleRegistry();
  }

  @Test
  void knownRulesPresent() {
    assertNotNull(registry.getRule("spaceBetweenTokens"));
    assertNotNull(registry.getRule("spaceBetweenOperator"));
  }

  @Test
  void getUnknownReturnsNull() {
    assertNull(registry.getRule("no-such-rule"));
  }

  @Test
  void getRule_handlesInternalException() throws Exception {
    java.util.Map<String, com.ingsis.format.rules.FormatRule> badMap =
        new java.util.HashMap<>() {
          @Override
          public com.ingsis.format.rules.FormatRule get(Object key) {
            throw new RuntimeException("boom");
          }
        };

    java.lang.reflect.Field f = RuleRegistry.class.getDeclaredField("rules");
    f.setAccessible(true);
    f.set(registry, badMap);

    org.junit.jupiter.api.Assertions.assertThrows(
        java.util.NoSuchElementException.class, () -> registry.getRule("anything"));
  }
}
