FROM ghcr.io/mulecode/tool-set-java:2.0.0.local as builder
WORKDIR /app
COPY ./data ./data
COPY ./src ./src
# Copy gradle files
COPY ./build.gradle .
COPY ./settings.gradle .
# Build the project TODO do not exclude tests
RUN gradle --no-daemon build -x test

FROM ghcr.io/mulecode/tool-set-java:2.0.0.local
WORKDIR /
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-server", "-jar", "app.jar"]
