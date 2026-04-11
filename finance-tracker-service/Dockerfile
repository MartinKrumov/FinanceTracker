# Multi-stage build for layer extraction
FROM eclipse-temurin:25-jre-alpine AS builder
WORKDIR /builder
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ---------- Runtime image ----------
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -g 1001 appuser && \
    adduser -D -u 1001 -G appuser appuser

# Copy layers in order of change frequency (least to most)
COPY --from=builder /builder/dependencies/ ./
COPY --from=builder /builder/snapshot-dependencies/ ./
COPY --from=builder /builder/spring-boot-loader/ ./
COPY --from=builder /builder/application/ ./

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8090

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8090/actuator/health || exit 1

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
