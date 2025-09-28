package io.github.bcarter97.spring_grpc_server.health;

import io.grpc.Status;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.stub.StreamObserver;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class HealthService extends HealthGrpc.HealthImplBase {

  private static final Logger logger = LoggerFactory.getLogger(HealthService.class);

  private final List<HealthCheckedService> services;

  public HealthService(List<HealthCheckedService> services) {
    this.services = services;
  }

  @Override
  public void check(HealthCheckRequest req, StreamObserver<HealthCheckResponse> resp) {
    String probe = req.getService();
    HealthCheckResponse.ServingStatus status;

    switch (probe) {
      case "startup" ->
          status =
              services.parallelStream().allMatch(svc -> svc.healthChecks().startup().check())
                  ? HealthCheckResponse.ServingStatus.SERVING
                  : HealthCheckResponse.ServingStatus.NOT_SERVING;

      case "readiness" ->
          status =
              services.parallelStream().allMatch(svc -> svc.healthChecks().readiness().check())
                  ? HealthCheckResponse.ServingStatus.SERVING
                  : HealthCheckResponse.ServingStatus.NOT_SERVING;

      case "liveness" -> status = HealthCheckResponse.ServingStatus.SERVING;

      default -> {
        logger.warn("Unknown probe '{}': returning NOT_FOUND", probe);
        resp.onError(
            Status.NOT_FOUND
                .withDescription("Use service=startup|readiness|liveness")
                .asRuntimeException());
        return;
      }
    }

    resp.onNext(HealthCheckResponse.newBuilder().setStatus(status).build());
    resp.onCompleted();

    if (status == HealthCheckResponse.ServingStatus.NOT_SERVING) {
      logger.warn("Probe '{}' failed -> NOT_SERVING", probe);
    }
  }

  @Override
  public void watch(HealthCheckRequest req, StreamObserver<HealthCheckResponse> resp) {
    logger.debug("Health watch requested for service '{}', not implemented", req.getService());
    resp.onError(Status.UNIMPLEMENTED.asRuntimeException());
  }
}
