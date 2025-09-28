package io.github.bcarter97.spring_grpc_server.health;

@FunctionalInterface
public interface Health {
  boolean check();

  static Health of(boolean healthy) {
    return () -> healthy;
  }

  Health NOOP = of(true);
}
