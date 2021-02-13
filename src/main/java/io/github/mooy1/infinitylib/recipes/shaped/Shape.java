package io.github.mooy1.infinitylib.recipes.shaped;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility enum of 'shapes', for example a helmet could be:
 *
 * 111    000
 * 101 or 111
 * 000    101
 */
enum Shape {

    SHAPELESS(120000000, 123000000),

    SINGLE(100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1),

    TWO_UP(100100000, 10010000, 1001000, 100100, 10010, 1001),
    TWO_SIDE(110000000, 11000000, 110000, 11000, 110, 11),

    THREE_UP(100100100, 10010010, 1001001),
    THREE_SIDE(111000000, 111000, 111),
    THREE_SWORD(100100200, 10010020, 1001002),
    THREE_ARROW(100200300, 10020030, 1002003),
    THREE_SHOVEL(100200200, 10020020, 1002002),
    THREE_SOULBOUND(10020010, 100200100, 1002001),

    FOUR_SQUARE(110110000, 11011000, 110110, 11011),
    FOUR_HOE(110200200, 110020020, 11020020, 11002002),
    FOUR_BOOTS(101101000, 101101),

    FIVE_AXE(110120020, 11012002, 110210200, 11021020),
    FIVE_HELMET(111101000, 111101),

    SIX_TABLE(111111000, 111111),
    SIX_DOOR(110110110, 11011011),
    SIX_STAIRS(1011111, 100110111),
    SIX_BOW(120102120, 12102012),

    FULL();

    private final int[] shapes;

    Shape(int... shapes) {
        this.shapes = shapes;
    }

    private static final Map<Integer, Shape> MAP = new HashMap<>();

    static {
        for (Shape shape : Shape.values()) {
            for (int i : shape.shapes) {
                MAP.put(i, shape);
            }
        }
    }
    
    static Shape get(int shape) {
        return MAP.getOrDefault(shape, Shape.FULL);
    }

}
