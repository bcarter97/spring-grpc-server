# ---- Build stage ----
FROM gradle:8.10.2-jdk-21 AS build
WORKDIR /workspace
COPY . .
RUN gradle clean extractBootLayers --no-daemon

# ---- Runtime stage ----
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /workspace/build/layers/dependencies/ ./dependencies/
COPY --from=build /workspace/build/layers/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=build /workspace/build/layers/resources/ ./resources/
COPY --from=build /workspace/build/layers/application/ ./application/
USER 65532:65532
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
