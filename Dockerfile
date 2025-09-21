# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Cache deps first
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle* gradle.properties* ./
RUN ./gradlew --version

# Then sources
COPY src src

# Build a single, executable Boot JAR named app.jar
RUN ./gradlew clean bootJar -x test --no-daemon

# ---------- Runtime stage (tiny, non-root) ----------
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /workspace/build/libs/app.jar /app/app.jar
USER 65532:65532
ENTRYPOINT ["java","-jar","/app/app.jar"]
