package io.github.bcarter97.spring_grpc_server.health;

@FunctionalInterface
public interface HealthCheckedService {
  HealthChecks healthChecks();
}
