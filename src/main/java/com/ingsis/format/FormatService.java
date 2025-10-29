package com.ingsis.format;

import com.ingsis.format.dto.CreateFormatDTO;
import com.ingsis.format.dto.GetFormatRule;
import com.ingsis.format.dto.Result;
import com.ingsis.format.dto.UpdateFormatDTO;
import com.ingsis.format.rules.FormatRule;
import com.ingsis.format.rules.RuleRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FormatService {

  private final FormatRepository formatRepository;
  private final RuleRegistry ruleRegistry;
  private static final Logger logger = LoggerFactory.getLogger(FormatService.class);

  public FormatService(RuleRegistry registry, FormatRepository formatRepository) {
    this.formatRepository = formatRepository;
    this.ruleRegistry = registry;
  }

  public ResponseEntity<Void> saveRules(List<CreateFormatDTO> ruleDTOs, String ownerId) {
    List<Format> lints = new ArrayList<>();
    for (CreateFormatDTO dto : ruleDTOs) {
      Format rule = formatRepository.findByNameAndOwnerId(dto.name(), ownerId);
      if (rule == null) {
        rule = new Format(ownerId, dto.name(), dto.defaultValue(), dto.active());
        logger.info("Created rule {} for owner {}", dto.name(), ownerId);
        if (!lints.contains(rule)) {
          lints.add(rule);
        }
        logger.info("Rule {} already declared for owner {}", rule, ownerId);
      }
      logger.info("rule {} already exists for owner {}", dto.name(), ownerId);
    }
    formatRepository.saveAll(lints);
    logger.info("Saving {} rules for owner {}", lints.size(), ownerId);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> updateRule(List<UpdateFormatDTO> updateLintingDTO, String ownerId) {
    for (UpdateFormatDTO dto : updateLintingDTO) {
      Format result = formatRepository.findByOwnerIdAndId(ownerId, dto.formatId());
      if (result == null) {
        logger.info("Rule {} not found for ownerId {}", dto.formatId(), ownerId);
        return ResponseEntity.badRequest().body(dto.formatId() + " not found");
      }
      result.setActive(dto.active());
      logger.info("{} rule", dto.active() ? "Enabled" : "Disabled");
      result.setDefaultValue(dto.value());
      logger.info("Updated value to {} for ownerId {}", dto.value(), ownerId);
      formatRepository.save(result);
      logger.info("Updated rule {} for ownerId {}", dto.formatId(), ownerId);
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Result> format(String contentData, String ownerId) {
    List<String> namesToRemove =
        List.of(
            "jumpAfterSemicolon",
            "indentationSameAsIf",
            "spaceBetweenTokens",
            "spaceBetweenOperator",
            "keyBracketAlone");

    List<Format> rules =
        formatRepository.findByOwnerIdAndActive(ownerId, true).stream()
            .filter(rule -> !namesToRemove.contains(rule.getName()))
            .collect(Collectors.toList());

    // Agregar las reglas obligatorias
    rules.add(formatRepository.findByNameAndOwnerId("indentationSameAsIf", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("spaceBetweenOperator", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("spaceBetweenTokens", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("keyBracketAlone", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("jumpAfterSemicolon", ownerId));

    logger.info("Found {} total format rules for owner {}", rules.size(), ownerId);

    String formattedContent = contentData;

    for (Format format : rules) {
      FormatRule rule = ruleRegistry.getRule(format.getName());
      if (rule == null) {
        logger.warn("Format rule {} not found in registry", format.getName());
        continue;
      }

      logger.info(
          "Applying rule {} to content (length: {})", format.getName(), formattedContent.length());

      String newContent = rule.apply(formattedContent, format.getDefaultValue());

      if (!newContent.equals(formattedContent)) {
        logger.info("Rule {} modified content.", format.getName());
      }

      formattedContent = newContent;
    }

    Result result = new Result(formattedContent);
    logger.info(
        "Format evaluation completed for owner {}. Final length: {}",
        ownerId,
        formattedContent.length());
    return ResponseEntity.ok(result);
  }

  public ResponseEntity<List<GetFormatRule>> getRulesByOwnerId(String ownerId) {
    logger.info("Fetching all linting rules for ownerId {}", ownerId);

    try {
      List<Format> rules = formatRepository.findByOwnerId(ownerId);

      if (rules == null || rules.isEmpty()) {
        logger.info("No linting rules found for ownerId {}", ownerId);
        return ResponseEntity.ok(Collections.emptyList());
      }
      logger.info("Found {} linting rules for ownerId {}", rules.size(), ownerId);
      List<GetFormatRule> lintRules = List.copyOf(convertToLintRule(rules));
      return ResponseEntity.ok(lintRules);

    } catch (Exception e) {
      logger.error("Error retrieving linting rules for ownerId {}: {}", ownerId, e.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }

  public List<GetFormatRule> convertToLintRule(List<Format> formatRules) {
    List<GetFormatRule> rules = new ArrayList<>();
    for(Format format : formatRules){
      rules.add(new GetFormatRule(format.getId(),format.getName(),format.isActive()));
    }
    return rules;
  }
}
