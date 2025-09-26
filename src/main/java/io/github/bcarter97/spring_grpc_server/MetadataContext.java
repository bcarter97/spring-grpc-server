package io.github.bcarter97.spring_grpc_server;

import io.grpc.Context;
import io.grpc.Metadata;

import java.util.Optional;

/**
 * Utility for accessing per-call inbound gRPC metadata via io.grpc.Context.
 * Interceptors should populate it using enrichFrom(Metadata) once at call start.
 */
public final class MetadataContext {

    private static final Context.Key<GrpcMetadata> KEY = Context.key("grpc-metadata");

    private MetadataContext() {
    }

    /**
     * Attach GrpcMetadata to the current Context, returning the new Context.
     */
    public static Context with(GrpcMetadata md) {
        return Context.current().withValue(KEY, (md == null) ? GrpcMetadata.empty() : md);
    }

    /**
     * Build GrpcMetadata from raw gRPC Metadata and attach it.
     */
    public static Context enrichFrom(Metadata raw) {
        return with(GrpcMetadata.fromGrpc(raw));
    }

    /**
     * Current GrpcMetadata (never null).
     */
    public static GrpcMetadata current() {
        GrpcMetadata md = KEY.get();
        return md == null ? GrpcMetadata.empty() : md;
    }

    /**
     * Convenience getter for a single header value.
     */
    public static Optional<String> get(String key) {
        return current().get(key);
    }
}

