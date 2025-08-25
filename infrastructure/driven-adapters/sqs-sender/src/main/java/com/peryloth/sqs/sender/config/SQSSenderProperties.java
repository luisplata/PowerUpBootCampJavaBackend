package com.peryloth.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
     String region,
     String requestQueueUrl,
     String responseQueueUrl,
     String endpoint){
}
