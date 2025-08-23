FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Crear el usuario y grupo 'appuser' con un UID/GID bajo
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser
COPY ../build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
# ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar Semana1.jar" ]


FROM gradle:jdk21-corretto AS TEMP_BUILD_IMAGE
ENV INSTALL_DIR=/opt/app
WORKDIR $INSTALL_DIR
COPY . $INSTALL_DIR
RUN gradle clean
RUN gradle bootJar

FROM eclipse-temurin:21-jre AS runtime

ENV INSTALL_DIR=/opt/app

ARG USERNAME=user
ARG USER_UID=999
ARG USER_GID=$USER_UID


WORKDIR $INSTALL_DIR

COPY --from=TEMP_BUILD_IMAGE $INSTALL_DIR/applications/app-service/build/libs/*.jar $INSTALL_DIR/app.jar

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && chown -R $USER_UID:$USER_GID $INSTALL_DIR

USER user


ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]