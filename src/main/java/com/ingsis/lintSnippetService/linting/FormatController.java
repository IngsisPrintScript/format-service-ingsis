package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateFormatDTO;
import com.ingsis.lintSnippetService.linting.dto.FormatSnippet;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateFormatDTO;
import java.util.*;

import com.ingsis.lintSnippetService.redis.dto.FormatStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/linting")
public class FormatController {

  private final FormatService formatService;

  public FormatController(FormatService lintingService) {
    this.formatService = lintingService;
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createLintRule(@RequestBody List<CreateFormatDTO> lintingDTO, @RequestParam String ownerId) {
      formatService.saveRules(lintingDTO, ownerId);
      return ResponseEntity.ok().build();
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateLintRule(@RequestBody List<UpdateFormatDTO> updateFormatDTO, @RequestParam String ownerId) {
    try {
      return ResponseEntity.ok(formatService.updateRule(updateFormatDTO,ownerId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/format")
  public ResponseEntity<Result> formatRules(
      @RequestBody FormatSnippet evaluateSnippet) {
    Result result =
        formatService.format(evaluateSnippet.content(), evaluateSnippet.ownerId()).getBody();
    return ResponseEntity.ok(result);
  }

}
