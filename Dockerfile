# ---- Build stage ----
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle clean extractBootLayers --no-daemon

# ---- Runtime stage ----
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /workspace/build/layers/ ./
USER 65532:65532
ENTRYPOINT ["java","-XX:MaxRAMPercentage=70","org.springframework.boot.loader.launch.JarLauncher"]
