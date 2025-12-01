package com.ingsis.format;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ingsis.format.dto.CreateFormatDTO;
import com.ingsis.format.dto.GetFormatRule;
import com.ingsis.format.dto.Result;
import com.ingsis.format.dto.UpdateFormatDTO;
import com.ingsis.format.rules.RuleRegistry;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class FormatServiceTest {

    private FormatRepository repo;
    private RuleRegistry registry;
    private FormatService service;

    @BeforeEach
    void setUp() {
        repo = mock(FormatRepository.class);
        registry = new RuleRegistry();
        service = new FormatService(registry, repo);
    }

    @Test
    void saveRulesCreatesNewOnes() {
        when(repo.findByNameAndOwnerId(anyString(), anyString())).thenReturn(null);
        List<CreateFormatDTO> dtos = List.of(new CreateFormatDTO("a", "v", true));
        ResponseEntity<Void> r = service.saveRules(dtos, "own");
        assertEquals(200, r.getStatusCodeValue());
        verify(repo).saveAll(anyList());
    }

    @Test
    void updateRuleNotFoundReturnsBadRequest() {
        when(repo.findByOwnerIdAndId(anyString(), any(UUID.class))).thenReturn(null);
        UpdateFormatDTO dto = new UpdateFormatDTO(UUID.randomUUID(), "x", true);
        ResponseEntity<?> r = service.updateRule(List.of(dto), "o");
        assertEquals(400, r.getStatusCodeValue());
    }

    @Test
    void updateRuleFoundSavesAndReturnsOk() {
        UUID id = UUID.randomUUID();
        Format existing = new Format("o", "someRule", "old", false);
        try {
            java.lang.reflect.Field f = Format.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(existing, id);
        } catch (Exception e) {
            fail("reflection setup failed");
        }

        when(repo.findByOwnerIdAndId("o", id)).thenReturn(existing);

        UpdateFormatDTO dto = new UpdateFormatDTO(id, "newVal", true);
        ResponseEntity<?> r = service.updateRule(List.of(dto), "o");
        assertEquals(200, r.getStatusCodeValue());

        verify(repo).save(argThat(saved -> saved.getDefaultValue().equals("newVal") && saved.isActive()));
    }

    @Test
    void getRulesByOwnerIdEmpty() {
        when(repo.findByOwnerId(anyString())).thenReturn(List.of());
        ResponseEntity<List<GetFormatRule>> r = service.getRulesByOwnerId("o");
        assertEquals(200, r.getStatusCodeValue());
        assertTrue(r.getBody().isEmpty());
    }

    @Test
    void convertToLintRuleCreatesRecords() {
        Format f = new Format("o", "n", "v", true);
        List<GetFormatRule> l = service.convertToLintRule(List.of(f));
        assertEquals(1, l.size());
        assertEquals("n", l.get(0).name());
    }

    @Test
    void getRulesByOwnerIdHandlesRepositoryException() {
        when(repo.findByOwnerId(anyString())).thenThrow(new RuntimeException("boom"));
        ResponseEntity<List<GetFormatRule>> r = service.getRulesByOwnerId("o");
        assertEquals(500, r.getStatusCodeValue());
    }

    @Test
    void formatUsesRulesAndReturnsResult() {
        Format f = new Format("o", "spaceBetweenTokens", " ", true);
        when(repo.findByOwnerIdAndActive("o", true)).thenReturn(List.of(f));
        when(repo.findByNameAndOwnerId("indentationSameAsIf", "o")).thenReturn(f);
        when(repo.findByNameAndOwnerId("spaceBetweenOperator", "o")).thenReturn(f);
        when(repo.findByNameAndOwnerId("spaceBetweenTokens", "o")).thenReturn(f);
        when(repo.findByNameAndOwnerId("keyBracketAlone", "o")).thenReturn(f);
        when(repo.findByNameAndOwnerId("jumpAfterSemicolon", "o")).thenReturn(f);

        ResponseEntity<Result> res = service.format("a ,b", "o");
        assertEquals(200, res.getStatusCodeValue());
        assertNotNull(res.getBody());
    }

    @Test
    void updateRuleProcessesMultipleDtos() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Format existing1 = new Format("o", "r1", "v1", false);
        Format existing2 = new Format("o", "r2", "v2", false);

        try {
            java.lang.reflect.Field f = Format.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(existing1, id1);
            f.set(existing2, id2);
        } catch (Exception e) {
            fail("reflection setup failed");
        }

        when(repo.findByOwnerIdAndId("o", id1)).thenReturn(existing1);
        when(repo.findByOwnerIdAndId("o", id2)).thenReturn(existing2);

        UpdateFormatDTO dto1 = new UpdateFormatDTO(id1, "new1", true);
        UpdateFormatDTO dto2 = new UpdateFormatDTO(id2, "new2", false);

        ResponseEntity<?> r = service.updateRule(List.of(dto1, dto2), "o");
        assertEquals(200, r.getStatusCodeValue());

        verify(repo, times(2)).save(any(Format.class));
    }
}
