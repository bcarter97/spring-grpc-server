package io.github.bcarter97.spring_grpc_server;

import io.grpc.Context;
import io.grpc.Metadata;
import java.util.Optional;

public final class MetadataContext {

  private static final Context.Key<GrpcMetadata> KEY = Context.key("grpc-metadata");

  public static Context with(GrpcMetadata md) {
    return Context.current().withValue(KEY, (md == null) ? GrpcMetadata.empty() : md);
  }

  public static Context enrichFrom(Metadata raw) {
    return with(GrpcMetadata.fromGrpc(raw));
  }

  public static GrpcMetadata current() {
    GrpcMetadata md = KEY.get();
    return md == null ? GrpcMetadata.empty() : md;
  }

  /** Convenience getter for a single header value. */
  public static Optional<String> get(String key) {
    return current().get(key);
  }
}
