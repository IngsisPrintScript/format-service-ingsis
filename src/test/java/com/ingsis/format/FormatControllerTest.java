package com.ingsis.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ingsis.format.dto.CreateFormatDTO;
import com.ingsis.format.dto.FormatSnippet;
import com.ingsis.format.dto.Result;
import com.ingsis.format.dto.UpdateFormatDTO;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class FormatControllerTest {

    private FormatService service;
    private FormatController controller;

    @BeforeEach
    void setUp() {
        service = mock(FormatService.class);
        controller = new FormatController(service);
    }

    @Test
    void createCallsService() {
        doReturn(ResponseEntity.ok().build()).when(service).saveRules(anyList(), anyString());
        ResponseEntity<Void> r = controller.createLintRule(List.of(new CreateFormatDTO("n", "v", true)), "o");
        assertEquals(200, r.getStatusCodeValue());
    }

  @Test
  void updateReturnsOkWhenServiceOk() {
        when(service.updateRule(anyList(), anyString())).thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> r =
            controller.updateLintRule(List.of(new UpdateFormatDTO(UUID.randomUUID(), "v", true)), "o");
        assertEquals(200, r.getStatusCodeValue());
  }

  @Test
  void updateHandlesServiceException() {
        when(service.updateRule(anyList(), anyString())).thenThrow(new RuntimeException("boom"));
        ResponseEntity<?> r =
            controller.updateLintRule(List.of(new UpdateFormatDTO(UUID.randomUUID(), "v", true)), "o");
        assertEquals(400, r.getStatusCodeValue());
  }

  @Test
  void updateHandlesNullFromService() {
        when(service.updateRule(anyList(), anyString())).thenReturn(null);
        ResponseEntity<?> r =
            controller.updateLintRule(List.of(new UpdateFormatDTO(UUID.randomUUID(), "v", true)), "o");
        assertEquals(200, r.getStatusCodeValue());
        assertNull(r.getBody());
  }

    @Test
    void createLintRuleHandlesNullList() {
        doReturn(ResponseEntity.ok().build()).when(service).saveRules(anyList(), anyString());
        ResponseEntity<Void> r = controller.createLintRule(List.of(), "o");
        assertEquals(200, r.getStatusCodeValue());
    }

  @Test
  void formatRoutesToService() {
        when(service.format(anyString(), anyString())).thenReturn(ResponseEntity.ok(new Result("x")));
        ResponseEntity<Result> r = controller.formatRules(new FormatSnippet("c", "o"));
        assertEquals(200, r.getStatusCodeValue());
        assertEquals("x", r.getBody().content());
  }

  @Test
  void getAllRulesDelegates() {
        when(service.getRulesByOwnerId("o")).thenReturn(ResponseEntity.ok(List.of()));
        ResponseEntity<List<com.ingsis.format.dto.GetFormatRule>> r = controller.getAllRules("o");
        assertEquals(200, r.getStatusCodeValue());
  }
}
