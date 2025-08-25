package com.peryloth.sqs.sender;

import com.peryloth.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SqsRequestResponseService {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public Mono<String> sendRequest(String payload) {
        String correlationId = UUID.randomUUID().toString();

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(properties.requestQueueUrl())
                .messageBody(payload)
                .messageAttributes(Map.of(
                        "correlationId",
                        MessageAttributeValue.builder().dataType("String").stringValue(correlationId).build(),
                        "replyToQueue",
                        MessageAttributeValue.builder().dataType("String").stringValue(properties.responseQueueUrl()).build()
                ))
                .build();

        return Mono.fromFuture(() -> client.sendMessage(request))
                .flatMap(resp -> {
                    log.debug("Request sent with id {}", resp.messageId());
                    return waitForResponse(correlationId);
                });
    }

    private Mono<String> waitForResponse(String correlationId) {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> Mono.fromFuture(() -> client.receiveMessage(r -> r
                        .queueUrl(properties.responseQueueUrl())
                        .messageAttributeNames("All")
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(2)
                )))
                .flatMapIterable(r -> r.messages())
                .filter(m -> correlationId.equals(m.messageAttributes().get("correlationId").stringValue()))
                .flatMap(m -> Mono.fromFuture(() -> client.deleteMessage(d -> d
                        .queueUrl(properties.responseQueueUrl())
                        .receiptHandle(m.receiptHandle())
                )).thenReturn(m.body()))
                .next()
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
    }
}
