package io.github.bcarter97.spring_grpc_server.health;

import java.util.List;
import java.util.concurrent.*;

public record HealthChecks(Health startup, Health readiness) {

  public static Health sequential(List<Health> checks) {
    return () -> checks.stream().allMatch(Health::check);
  }

  public static Health parallel(List<Health> checks) {
    return () -> checks.parallelStream().allMatch(Health::check);
  }
}
