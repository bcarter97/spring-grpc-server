package io.github.bcarter97.spring_grpc_server;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcGlobalServerInterceptor
public class MetadataInterceptor implements ServerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(MetadataInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

    GrpcMetadata grpcMetadata = GrpcMetadata.fromGrpc(headers);
    Context ctx = MetadataContext.with(grpcMetadata);

    logger.debug(
        "Inbound method={} grpcMetadata={}",
        call.getMethodDescriptor().getFullMethodName(),
        grpcMetadata);

    return Contexts.interceptCall(ctx, call, headers, next);
  }
}
