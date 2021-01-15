package io.github.mooy1.infinitylib.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Objects;

@Getter
@AllArgsConstructor
public final class Pair<A, B> {

    @Nullable
    private final A a;
    @Nullable
    private final B b;

    @Override
    public int hashCode() {
        int hash = 1;
        if (this.a != null) {
            hash *= this.a.hashCode();
        }
        if (this.b != null) {
            hash *= this.b.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

}
