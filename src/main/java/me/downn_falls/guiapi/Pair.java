package me.downn_falls.guiapi;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(@NotNull K var1, @NotNull V var2) {
        this.key = var1;
        this.value = var2;
    }

    public static <K, V> Pair<K, V> of(@NotNull K var0, @NotNull V var1) {
        return new Pair<>(var0, var1);
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Pair<?, ?> pair = (Pair)o;
            return this.key.equals(pair.key) && this.value.equals(pair.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

}
