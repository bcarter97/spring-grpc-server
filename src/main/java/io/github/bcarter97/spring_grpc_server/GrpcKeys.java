package io.github.bcarter97.spring_grpc_server;

import io.grpc.Context;

public final class GrpcKeys {
    public static final Context.Key<String> CLIENT_ID = Context.key("client-id");
}
