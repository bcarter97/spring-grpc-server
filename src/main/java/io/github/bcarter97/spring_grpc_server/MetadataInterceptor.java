package io.github.bcarter97.spring_grpc_server;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class MetadataInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        Context ctx = MetadataContext.enrichFrom(headers);

        System.out.println("Inbound method=" + call.getMethodDescriptor().getFullMethodName() +
                " headerKeys=" + headers.keys());

        return Contexts.interceptCall(ctx, call, headers, next);
    }
}