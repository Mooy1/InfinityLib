package io.github.mooy1.infinitylib.slimefun;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ParametersAreNonnullByDefault
public final class Duo<F, S> {

    F first;
    S second;

}
