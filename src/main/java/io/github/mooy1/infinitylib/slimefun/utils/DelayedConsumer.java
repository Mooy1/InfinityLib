package io.github.mooy1.infinitylib.slimefun.utils;

import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A consumer which stores elements which have been accepted for acceptance at a later time
 * 
 * @see DelayedRecipeType
 * 
 * @author Mooy1
 */
public final class DelayedConsumer<A, B> implements BiConsumer<A, B> {

    private List<Pair<A, B>> toBeAccepted = new ArrayList<>();
    private BiConsumer<A, B> consumer = null;

    public DelayedConsumer() {}

    @Override
    public void accept(A a, B b) {
        if (this.consumer == null) {
            this.toBeAccepted.add(new Pair<>(a, b));
        } else {
            this.consumer.accept(a, b);
        }
    }

    public void acceptEach(BiConsumer<A, B> consumer) {
        this.consumer = consumer;
        Iterator<Pair<A, B>> iterator = this.toBeAccepted.listIterator();
        while (iterator.hasNext()) {
            Pair<A, B> next = iterator.next();
            consumer.accept(next.getFirstValue(), next.getSecondValue());
            iterator.remove();
        }
        this.toBeAccepted = null;
    }

}
