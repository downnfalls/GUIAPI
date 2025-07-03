package me.downn_falls.guiapi;

@FunctionalInterface
public interface TriConsumer<T, U, V, R> {
    R accept(T t, U u, V v);
}
