package io.github.mooy1.infinitylib.machines;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class MachineLayout {

    public static final MachineLayout DEFAULT = new MachineLayout(
            new int[] { 9, 10, 11, 12, 18, 21, 27, 28, 29, 30 }, new int[] { 19, 20 },
            new int[] { 14, 15, 16, 17, 23, 26, 32, 33, 34, 35 }, new int[] { 24, 25 },
            new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44 }, 22
    );

    private final int[] inputBorder;
    private final int[] inputSlots;
    private final int[] outputBorder;
    private final int[] outputSlots;
    private final int[] background;
    private final int statusSlot;

}
