package io.github.bcarter97.spring_grpc_server;

import com.example.demo.grpc.GreeterGrpc;
import com.example.demo.grpc.HelloReply;
import com.example.demo.grpc.HelloRequest;
import io.github.bcarter97.spring_grpc_server.health.Health;
import io.github.bcarter97.spring_grpc_server.health.HealthCheckedService;
import io.github.bcarter97.spring_grpc_server.health.HealthChecks;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.Optional;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase implements HealthCheckedService {

  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
    GrpcMetadata md = MetadataContext.current();
    Optional<String> clientId = md.get("client-id");

    var reply =
        HelloReply.newBuilder()
            .setMessage(
                "Hello "
                    + request.getName()
                    + clientId.map(id -> " (client: " + id + ")").orElse(""))
            .build();

    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public HealthChecks healthChecks() {
    Health startup = HealthChecks.parallel(List.of(Health.NOOP, Health.NOOP));
    Health readiness = Health.NOOP;
    return new HealthChecks(startup, readiness);
  }
}
