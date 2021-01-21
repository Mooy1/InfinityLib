package io.github.mooy1.infinitylib.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Pair<A, B> {

    private A a;
    private B b;

}
