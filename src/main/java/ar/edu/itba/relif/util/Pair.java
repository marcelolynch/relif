package ar.edu.itba.relif.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an ordered pair
 */
public class Pair<A, B> implements Serializable {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        this.first = first;
        this.second = second;
    }

    public static <A,B> Pair<A,B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    /**
     * @return the first element of this pair
     */
    public A getFirst() {
        return first;
    }

    /**
     * @return the second element of this pair
     */
    public B getSecond() {
        return second;
    }


    /**
     * An alias for {@link #getFirst}.
     * @return the first element of this pair
     */
    public A fst() {
        return getFirst();
    }

    /**
     * An alias for {@link #getSecond}.
     * @return the second element of this pair
     */
    public B snd() {
        return getSecond();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
