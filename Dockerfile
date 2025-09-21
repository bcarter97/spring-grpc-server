# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle* gradle.properties* ./
RUN chmod +x gradlew && ./gradlew --version
COPY src src
# Use BuildKit caches for much faster rebuilds
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    ./gradlew clean bootJar -x test --no-daemon

# ---------- Runtime stage ----------
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /workspace/build/libs/app.jar /app/app.jar
USER 65532:65532
ENTRYPOINT ["java","-jar","/app/app.jar"]
