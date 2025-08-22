FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Crear el usuario y grupo 'appuser' con un UID/GID bajo
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser
COPY ../build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
# ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar Semana1.jar" ]
