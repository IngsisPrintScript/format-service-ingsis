package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateFormatDTO;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateFormatDTO;
import com.ingsis.lintSnippetService.rules.FormatRule;
import com.ingsis.lintSnippetService.rules.RuleRegistry;
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
        if (!lints.contains(rule)){
          lints.add(rule);
        }logger.info("Rule {} already declared for owner {}", rule, ownerId);
      }
      logger.info("rule {} already exists for owner {}", dto.name(), ownerId);
    }
    formatRepository.saveAll(lints);
    logger.info("Saving {} rules for owner {}", lints.size(), ownerId);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> updateRule(List<UpdateFormatDTO> updateLintingDTO, String ownerId) {
    for (UpdateFormatDTO dto : updateLintingDTO) {
      Format result =
          formatRepository
              .findByOwnerIdAndId(ownerId,dto.lintId());
      if(result == null){
        logger.info("Rule {} not found for ownerId {}", dto.lintId(), ownerId);
        return ResponseEntity.badRequest().body(dto.lintId() + " not found");
      }
      result.setActive(dto.active());
      logger.info("{} rule", dto.active() ? "Enabled" : "Disabled");
      result.setDefaultValue(dto.value());
      logger.info("Updated value to {} for ownerId {}", dto.value(), ownerId);
      formatRepository.save(result);
      logger.info("Updated rule {} for ownerId {}", dto.lintId(), ownerId);
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Result> format(String contentData, String ownerId) {
    logger.info("Code to format: {}", contentData);

    List<String> namesToRemove = List.of(
            "jumpAfterSemicolon",
            "indentationSameAsIf",
            "spaceBetweenTokens",
            "spaceBetweenOperator",
            "KeyBracketAlone"
    );

    List<Format> rules = formatRepository.findByOwnerIdAndActive(ownerId, true)
            .stream()
            .filter(rule -> !namesToRemove.contains(rule.getName()))
            .collect(Collectors.toList());
    rules.add(formatRepository.findByNameAndOwnerId("jumpAfterSemicolon", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("indentationSameAsIf", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("spaceBetweenOperator", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("spaceBetweenTokens", ownerId));
    rules.add(formatRepository.findByNameAndOwnerId("keyBracketAlone", ownerId));
    logger.info("Found {} active format rules for owner {}", rules.size(), ownerId);
    Result result = null;
    String[] lines = contentData.split("\\r?\\n");

    for (Format format : rules) {
      FormatRule rule = ruleRegistry.getRule(format.getName());
      if (rule == null) {
        logger.warn("Format rule {} not found in registry", format.getName());
        continue;
      }

      logger.info("Applying rule {} to {} lines", format.getName(), lines.length);
      List<String> formattedLines = new ArrayList<>();

      for (String line : lines) {
        formattedLines.add(rule.apply(line,format.getDefaultValue()));
      }
      String formattedContent = String.join("", formattedLines);
      result = new Result(formattedContent);
      logger.info("Format evaluation completed for owner {} with {} results", ownerId, result.content());
    }
    return ResponseEntity.ok(result);
  }
}
