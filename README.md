# anotaciones de proyecto

## problemas

- No se pudo ejecutar migraciones con flyway de forma exitosa; se ejecutaron los SQL de forma manual en la base de datos.
- No se como es la estrategia de conexion por colas para enviar y recibir por colas separadas para realizar una comunicacion entre microservicios.


## TODO

- Terminar el flujo de escucha y renvio de informacion por colas.
- Revisar la covertura: Como se mira el porcentaje

### examples

#### SQS
```
aws --endpoint-url=http://localhost:4566 --profile localstack sqs send-message --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/usuario-request-queue --message-body '{"correlationId":"test2","replyToQueue":"http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/usuario-response-queue","timestamp":"2025-08-25T18:00:00Z","payload":{"action":"QUERY_USER_BY_EMAIL","email":"test@example.com"}}' --message-attributes '{"correlationId":{"DataType":"String","StringValue":"test2"},"replyToQueue":{"DataType":"String","StringValue":"http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/usuario-response-queue"}}'
```