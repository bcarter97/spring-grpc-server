package io.github.bcarter97.spring_grpc_server;

import com.example.demo.grpc.GreeterGrpc;
import com.example.demo.grpc.HelloReply;
import com.example.demo.grpc.HelloRequest;
import io.grpc.stub.StreamObserver;
import java.util.Optional;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

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
}
