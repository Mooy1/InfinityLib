package io.github.mooy1.infinitylib.recipes.shaped;

import io.github.mooy1.infinitylib.items.StackUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A shaped, 3x3 recipe which detects the shape on its own and compares ids and shape
 */
final class ShapedRecipe {

    private final String[] ids;
    private final Shape shape;
    private final int hashCode;

    ShapedRecipe(@Nonnull ItemStack[] items) {

        // map of ids to digit for shape
        Map<String, Integer> map = new TreeMap<>();

        // array of ids for 3x3 shaped recipes
        String[] ids = new String[9];

        // hash
        int hashCode = 0;
        
        // the shape int
        int shapeInt = 0;

        // the shape
        Shape shape;

        // the current digit
        AtomicInteger digit = new AtomicInteger(1);

        // find all ids and the shape int
        for (int i = 0 ; i < 9 ; i++) {
            shapeInt *= 10;
            if (items[i] != null) {
                String id = ids[i] = StackUtils.getIDorType(items[i]);
                shapeInt += map.computeIfAbsent(id, k -> digit.getAndIncrement());
                hashCode += id.hashCode();
            }
        }

        // find shape
        shape = Shape.get(shapeInt);
        
        if (shape != Shape.FULL) {
            // full should keep the current ids & hash
            ids = new String[map.size()];
            hashCode = 0;
            if (shape == Shape.SHAPELESS) {
                // sort tree map entries into array
                int i = 0;
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    ids[i++] = entry.getKey();
                    hashCode += entry.getValue().hashCode();
                }
            } else {
                // just use the tree map entries
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    ids[entry.getValue() - 1] = entry.getKey();
                    hashCode += entry.getValue().hashCode();
                }
            }
        }

        this.shape = shape;
        this.ids = ids;
        this.hashCode = hashCode + shape.hashCode();
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof ShapedRecipe)) {
            return false;
        }
        ShapedRecipe recipe = (ShapedRecipe) obj;
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

}
