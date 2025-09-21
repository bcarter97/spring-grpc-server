package io.github.bcarter97.spring_grpc_server;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class LoggingInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String clientId = headers.get(Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER));
        System.out.println("Got client-id header: " + clientId);

        Context ctx = Context.current().withValue(GrpcKeys.CLIENT_ID, clientId);
        return Contexts.interceptCall(ctx, call, headers, next);
    }
}