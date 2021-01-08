package io.github.mooy1.infinitylib.misc;

import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class DelayedConsumer<A, B> implements BiConsumer<A, B> {

    private List<Pair<A, B>> toBeAccepted = new ArrayList<>();
    private BiConsumer<A, B> consumer = null;

    public DelayedConsumer() { }

    public void accept(A a, B b) {
        if (this.consumer == null) {
            this.toBeAccepted.add(new Pair<>(a, b));
        } else {
            this.consumer.accept(a, b);
        }
    }

    public void acceptEach(BiConsumer<A, B> consumer) {
        this.consumer = consumer;
        for (Pair<A, B> pair : this.toBeAccepted) {
            consumer.accept(pair.getFirstValue(), pair.getSecondValue());
        }
        this.toBeAccepted = null;
    }

}
