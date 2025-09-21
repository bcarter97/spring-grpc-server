package io.github.bcarter97.spring_grpc_server;

import com.example.demo.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        Optional<String> clientId = Optional.ofNullable(GrpcKeys.CLIENT_ID.get());

        var reply = HelloReply.newBuilder()
                .setMessage("Hello " + request.getName() +
                        clientId.map(id -> " (client: " + id + ")").orElse(""))
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
