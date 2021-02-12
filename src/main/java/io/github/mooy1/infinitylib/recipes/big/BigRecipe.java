package io.github.mooy1.infinitylib.recipes.big;

import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

class BigRecipe {
    
    private final String[] ids;
    private final Shape shape;
    private final int hashCode;
    
    static Pair<BigRecipe, int[]> fromMenu(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        ItemStack[] stacks = new ItemStack[slots.length];
        int[] amounts = new int[slots.length];
        for (int i = 0 ; i < slots.length ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                amounts[i] = (stacks[i] = item).getAmount();
            }
        }
        return new Pair<>(new BigRecipe(stacks), amounts);
    }
    
    BigRecipe(@Nonnull ItemStack[] items) {
        Validate.isTrue(items.length == 9);
        
        Map<String, Integer> map = new TreeMap<>();
        String[] ids = new String[9];
        int shapeInt = 0;
        Shape shape = null;
        
        AtomicInteger next = new AtomicInteger(1);
        
        for (int i = 0; i < 9 ; i++, shapeInt*=10) {
            if (items[i] != null) {
                shapeInt += map.computeIfAbsent(ids[i] = StackUtils.getIDorType(items[i]), k -> next.getAndIncrement());
                if (next.intValue() == 4) {
                    shape = Shape.FULL;
                    break;
                }
            }
        }

        if (shape == null) {
            shape = Shape.MAP.getOrDefault(shapeInt, Shape.FULL);
        }

        int hashCode = 0;
        if (shape == Shape.FULL) {
            for (String id : ids) {
                if (id != null) {
                    hashCode += id.hashCode();
                }
            }
        } else {
            ids = new String[map.size()];
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                ids[entry.getValue() - 1] = entry.getKey();
                hashCode += entry.getValue().hashCode();
            }
        }
        
        this.shape = shape;
        this.ids = ids;
        this.hashCode = hashCode + shape.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BigRecipe)) {
            return false;
        }
        BigRecipe recipe = (BigRecipe) obj;
        if (recipe.shape != this.shape) {
            return false;
        }
        for (int i = 0 ; i < this.ids.length ; i++) {
            if (!Objects.equals(recipe.ids[i], this.ids[i])) {
                return false;
            }
        }
        return true;
    }
    
    private enum Shape {
        
        SHAPELESS(120000000, 123000000),
        
        SINGLE(100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1), 
        
        TWO_UP(100100000, 10010000, 1001000, 100100, 10010, 1001),
        TWO_SIDE(110000000, 11000000, 110000, 11000, 110, 11),
        
        THREE_UP(100100100, 10010010, 1001001),
        THREE_SIDE(111000000, 111000, 111),
        THREE_SWORD(100100200, 10010020, 1001002),
        THREE_ARROW(100200300, 10020030, 1002003),
        THREE_SHOVEL(100200200, 10020020, 1002002),
        
        FOUR_SQUARE(110110000, 11011000, 110110, 11011),
        FOUR_HOE(110200200, 110020020, 11020020, 11002002),
        
        FIVE_AXE(110120020, 11012002, 110210200, 11021020),
        
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
            for (Shape shape : values()) {
                for (int i : shape.shapes) {
                    MAP.put(i, shape);
                }
            }
        }
        
    }

}
