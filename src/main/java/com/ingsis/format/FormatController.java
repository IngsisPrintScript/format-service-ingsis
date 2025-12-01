package com.ingsis.format;

import com.ingsis.format.dto.CreateFormatDTO;
import com.ingsis.format.dto.FormatSnippet;
import com.ingsis.format.dto.GetFormatRule;
import com.ingsis.format.dto.Result;
import com.ingsis.format.dto.UpdateFormatDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/formater")
public class FormatController {

    private final FormatService formatService;

    public FormatController(FormatService lintingService) {
        this.formatService = lintingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createLintRule(@RequestBody List<CreateFormatDTO> lintingDTO,
            @RequestParam String ownerId) {
        formatService.saveRules(lintingDTO, ownerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLintRule(@RequestBody List<UpdateFormatDTO> updateFormatDTO,
            @RequestParam String ownerId) {
        try {
            return ResponseEntity.ok(formatService.updateRule(updateFormatDTO, ownerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/format")
    public ResponseEntity<Result> formatRules(@RequestBody FormatSnippet evaluateSnippet) {
        Result result = formatService.format(evaluateSnippet.content(), evaluateSnippet.ownerId()).getBody();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<GetFormatRule>> getAllRules(@RequestParam String ownerId) {
        return formatService.getRulesByOwnerId(ownerId);
    }
}
