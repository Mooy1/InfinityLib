package io.github.mooy1.infinitylib.recipes;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RecipeMap {

    private final Map<Object, Object> map = new HashMap<>();
    private final RecipeMapType type;
    

}
