package io.github.bcarter97.spring_grpc_server;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class MetadataInterceptor implements ServerInterceptor {
  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

    GrpcMetadata grpcMetadata = GrpcMetadata.fromGrpc(headers);
    Context ctx = MetadataContext.with(grpcMetadata);

    System.out.println(
        "Inbound method="
            + call.getMethodDescriptor().getFullMethodName()
            + " grpcMetadata="
            + grpcMetadata);

    return Contexts.interceptCall(ctx, call, headers, next);
  }
}
