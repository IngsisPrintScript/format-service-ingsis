package com.ingsis.lintSnippetService.redis;

import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.redis.dto.FormatResultEvent;
import com.ingsis.lintSnippetService.redis.dto.LintRequestEvent;
import com.ingsis.lintSnippetService.redis.dto.FormatStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.lintSnippetService.linting.FormatService;
import jakarta.annotation.PreDestroy;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Profile("!test")
public class FormatRequestConsumer extends RedisStreamConsumer<String>{

    private static final Logger logger = LoggerFactory.getLogger(FormatRequestConsumer.class);

    private final FormatService formatService;
    private final FormatResultProducer formatResultProducer;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public FormatRequestConsumer(
            @Value("${redis.streams.formatRequest}") String streamName,
            @Value("${redis.groups.format}") String groupName,
            RedisTemplate<String, String> redisTemplate,
            FormatService formatService,
            FormatResultProducer formatResultProducer,
            ObjectMapper objectMapper) {

        super(streamName, groupName, redisTemplate);
        this.formatService = formatService;
        this.formatResultProducer = formatResultProducer;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(@NotNull ObjectRecord<String, String> record) {
        executor.submit(() -> {
            try {
                LintRequestEvent event = objectMapper.readValue(record.getValue(), LintRequestEvent.class);
                logger.info("Processing lint request for Snippet({}) from User({})",
                        event.snippetId().toString(), event.ownerId());

                ResponseEntity<Result> response = formatService.format(event.content(), event.ownerId());
                Result results = response.getBody();
                FormatStatus status = (results != null)
                        ? FormatStatus.PASSED
                        : FormatStatus.FAILED;

                formatResultProducer.publish(new FormatResultEvent(
                        event.ownerId(),
                        event.snippetId(),
                        status,
                        results == null ? "" : results.content()
                ));
            } catch (Exception ex) {
                logger.error("Error processing lint request: {}", ex.getMessage());
            }
            return null;
        });
    }

    @Override
    public @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> options() {
        return StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(java.time.Duration.ofSeconds(10))
                .targetType(String.class)
                .build();
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

}