package io.github.mooy1.infinitylib.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Objects;

@Getter
@AllArgsConstructor
public final class Triplet<A, B, C> {

    private final A a;
    private final B b;
    private final C c;
    
    @Override
    public int hashCode() {
        int hash = 1;
        if (this.a != null) {
            hash *= this.a.hashCode();
        }
        if (this.b != null) {
            hash *= this.b.hashCode();
        }
        if (this.c != null) {
            hash *= this.c.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triplet)) {
            return false;
        }
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) obj;
        return Objects.equals(this.a, triplet.a) && Objects.equals(this.b, triplet.b) && Objects.equals(this.c, triplet.c);
    }
    
}
