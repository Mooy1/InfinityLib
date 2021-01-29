package io.github.mooy1.infinitylib.misc;

import javax.annotation.Nonnull;

public class Choice<A, B> {
    
    private final A a;
    private final B b;
    
    private Choice(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    public static <A, B> Choice<A, B> ofA(@Nonnull A a) {
        return new Choice<>(a, null);
    }

    public static <A, B> Choice<A, B> ofB(@Nonnull B b) {
        return new Choice<>(null, b);
    }
    
    public void accept(Acceptor<A> acceptorA, Acceptor<B> acceptorB) {
        if (this.a != null) {
            acceptorA.accept(this.a);
        } else if (this.b != null) {
            acceptorB.accept(this.b);
        }
    }
    
    @FunctionalInterface
    public interface Acceptor<T> {
        void accept(T t);
    }
        
}
