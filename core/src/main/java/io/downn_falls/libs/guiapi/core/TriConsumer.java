package io.downn_falls.libs.guiapi.core;

@FunctionalInterface
public interface TriConsumer<T, U, V, R> {
    R accept(T t, U u, V v);
}
