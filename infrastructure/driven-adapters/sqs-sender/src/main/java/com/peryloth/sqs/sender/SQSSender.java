package com.peryloth.sqs.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peryloth.sqs.sender.config.SQSSenderProperties;
import com.peryloth.sqs.contracts.request.UserQueryRequest;
import com.peryloth.sqs.contracts.request.UserQueryPayload;
import com.peryloth.sqs.contracts.response.UserQueryResponse;
import com.peryloth.sqs.contracts.response.UserResponsePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;

@Service
@Log4j2
public class SQSSender /*implements SomeGateway*/ {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public SQSSender(SQSSenderProperties properties, SqsAsyncClient client) {
        this.properties = properties;
        this.client = client;
        log.info("Constructor de SQSSender ejecutado, bean creado correctamente");
    }

    @PostConstruct
    public void startListening() {
        log.info("Inicializando SQS listener para la cola: {}", properties.requestQueueUrl());
        Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> Mono.fromFuture(() ->
                        client.receiveMessage(r -> r
                                .queueUrl(properties.requestQueueUrl())
                                .messageAttributeNames("All")
                                .maxNumberOfMessages(10)
                                .waitTimeSeconds(2)
                        )
                ))
                .flatMapIterable(resp -> resp.messages())
                .flatMap(this::processMessage)
                .doOnSubscribe(sub -> log.info("SQS listener suscrito y listo para recibir mensajes..."))
                .subscribe(
                        msgId -> log.debug("Processed message successfully: {}", msgId),
                        err -> log.error("Error processing message", err)
                );
    }

    private Mono<String> processMessage(software.amazon.awssdk.services.sqs.model.Message m) {
        log.info("Mensaje recibido desde SQS: id={}, body={}", m.messageId(), m.body());
        String payload = m.body();
        String replyQueue = m.messageAttributes().get("replyToQueue").stringValue();
        String correlationId = m.messageAttributes().get("correlationId").stringValue();

        // Procesar lógica de negocio
        String response = processPayload(payload);

        // Enviar respuesta
        SendMessageRequest responseRequest = SendMessageRequest.builder()
                .queueUrl(replyQueue)
                .messageBody(response)
                .messageAttributes(Map.of(
                        "correlationId", MessageAttributeValue.builder()
                                .stringValue(correlationId)
                                .dataType("String")
                                .build()
                ))
                .build();

        // Delete del mensaje original tras enviar la respuesta
        return Mono.fromFuture(() -> client.sendMessage(responseRequest))
                .flatMap(resp -> Mono.fromFuture(() ->
                        client.deleteMessage(d -> d
                                .queueUrl(properties.requestQueueUrl())
                                .receiptHandle(m.receiptHandle())
                        )
                ))
                .thenReturn(m.messageId());
    }

    private String processPayload(String payload) {
        try {
            // 1. Deserializar JSON recibido -> UserQueryRequest
            ObjectMapper mapper = new ObjectMapper();
            UserQueryRequest request = mapper.readValue(payload, UserQueryRequest.class);

            log.info("Request recibido: {}", request);

            // 2. Procesar la lógica
            // Ejemplo: simulamos que buscamos un usuario por email
            UserQueryResponse response = getUserQueryResponse(request);

            // 5. Serializar la respuesta
            return mapper.writeValueAsString(response);

        } catch (Exception e) {
            log.error("Error procesando payload", e);
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    private static UserQueryResponse getUserQueryResponse(UserQueryRequest request) {
        String email = request.getPayload().getEmail();
        // Aquí podrías llamar a tu servicio de usuario real
        Long userId = 123L;
        String name = "Pery Loth";

        // 3. Construir UserResponsePayload
        UserResponsePayload responsePayload = new UserResponsePayload();
        responsePayload.setUserId(userId);
        responsePayload.setName(name);
        responsePayload.setEmail(email);

        // 4. Construir UserQueryResponse
        UserQueryResponse response = new UserQueryResponse();
        response.setCorrelationId(request.getCorrelationId());
        response.setTimestamp(request.getTimestamp());
        response.setStatus("SUCCESS");
        response.setPayload(responsePayload);
        return response;
    }

}
