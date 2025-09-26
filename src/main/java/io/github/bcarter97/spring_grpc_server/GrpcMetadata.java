package io.github.bcarter97.spring_grpc_server;

import io.grpc.Metadata;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable single-valued ASCII gRPC metadata view.
 * Represents headers as Map<String, Optional<String>>.
 * Assumptions:
 * - Binary keys (ending with "-bin") are ignored.
 */
public final class GrpcMetadata {

    private final Map<String, Optional<String>> entries;

    private static final GrpcMetadata EMPTY = new GrpcMetadata(Collections.emptyMap());

    private GrpcMetadata(Map<String, Optional<String>> source) {
        Map<String, Optional<String>> normalized = normalize(source);
        this.entries = normalized.isEmpty() ? Map.of() : Collections.unmodifiableMap(normalized);
    }

    private static Map<String, Optional<String>> normalize(Map<String, Optional<String>> source) {
        return source.entrySet().stream()
                .filter(e -> !e.getKey().endsWith("-bin"))
                .map(e -> Map.entry(e.getKey().toLowerCase(Locale.ROOT), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    public static GrpcMetadata empty() {
        return EMPTY;
    }

    public static GrpcMetadata of(Map<String, Optional<String>> entries) {
        return entries == null || entries.isEmpty() ? EMPTY : new GrpcMetadata(entries);
    }

    public static GrpcMetadata fromGrpc(Metadata md) {
        if (md == null) return EMPTY;
        Map<String, Optional<String>> map = md.keys().stream()
                .filter(k -> !k.endsWith("-bin"))
                .collect(Collectors.toMap(k -> k, k -> Optional.ofNullable(md.get(Metadata.Key.of(k, Metadata.ASCII_STRING_MARSHALLER))), (a, b) -> b, LinkedHashMap::new));
        return of(map);
    }

    private static Metadata.Key<String> toMetadataKey(String rawKey) {
        return Metadata.Key.of(rawKey, Metadata.ASCII_STRING_MARSHALLER);
    }

    public Optional<String> get(String key) {
        return entries.getOrDefault(key.toLowerCase(Locale.ROOT), Optional.empty());
    }

    public boolean contains(String key) {
        return entries.containsKey(key.toLowerCase(Locale.ROOT));
    }

    public Map<String, Optional<String>> asMap() {
        return entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public String toString() {
        return entries.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue().orElse("<empty>"))
                .collect(Collectors.joining(", ", "GrpcMetadata{", "}"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrpcMetadata that)) return false;
        return entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }
}
