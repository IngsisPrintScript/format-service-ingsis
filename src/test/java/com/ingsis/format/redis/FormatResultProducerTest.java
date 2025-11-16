package com.ingsis.format.redis;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.format.redis.dto.FormatResultEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

class FormatResultProducerTest {

  private RedisTemplate<String, String> redis;
  private ObjectMapper mapper;
  private FormatResultProducer producer;

  @BeforeEach
  void setUp() {
    redis = mock(RedisTemplate.class);
    mapper = new ObjectMapper();
    producer = new FormatResultProducer("stream", redis, mapper);
  }

  @Test
  void publishSerializesAndPushes() {
    FormatResultEvent e = new FormatResultEvent("o", java.util.UUID.randomUUID(), null, "x");
    producer.publish(e);
  }
}
