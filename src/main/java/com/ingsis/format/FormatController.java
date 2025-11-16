package com.ingsis.format;

import com.ingsis.format.dto.*;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formater")
public class FormatController {

  private final FormatService formatService;

  public FormatController(FormatService lintingService) {
    this.formatService = lintingService;
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createLintRule(
      @RequestBody List<CreateFormatDTO> lintingDTO, @RequestParam String ownerId) {
    formatService.saveRules(lintingDTO, ownerId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateLintRule(
      @RequestBody List<UpdateFormatDTO> updateFormatDTO, @RequestParam String ownerId) {
    try {
      return ResponseEntity.ok(formatService.updateRule(updateFormatDTO, ownerId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/format")
  public ResponseEntity<Result> formatRules(@RequestBody FormatSnippet evaluateSnippet) {
    Result result =
        formatService.format(evaluateSnippet.content(), evaluateSnippet.ownerId()).getBody();
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<List<GetFormatRule>> getAllRules(@RequestParam String ownerId) {
    return formatService.getRulesByOwnerId(ownerId);
  }
}
