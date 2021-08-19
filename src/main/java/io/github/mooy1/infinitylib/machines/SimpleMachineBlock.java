package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;
import lombok.experimental.Accessors;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.recipes.SimpleRecipes;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public final class SimpleMachineBlock extends AbstractMachine {

    public static final int[] DEFAULT_INPUT = { 10 };
    public static final int[] DEFAULT_INPUT_BORDER = { 0, 1, 2, 9, 11, 18, 19, 20 };
    public static final int DEFAULT_STATUS = 13;
    public static final int[] DEFAULT_BACKGROUND = { 3, 4, 5, 12, 14, 21, 22, 23 };
    public static final int[] DEFAULT_OUTPUT = { 16 };
    public static final int[] DEFAULT_OUTPUT_BORDER = { 6, 7, 8, 15, 17, 24, 25, 26 };

    private final SimpleRecipes recipes = new SimpleRecipes();

    public SimpleMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, outputBorder, inputBorder, background, outputs, inputs, statusSlot);
    }

    @Nonnull
    public SimpleMachineBlock addRecipe(@Nullable ItemStack input, @Nullable ItemStack output) {
        this.recipes.addRecipe(input, output);
        return this;
    }

    @Nonnull
    public SimpleMachineBlock copyRecipesFrom(SimpleMachineBlock toCopy) {
        this.recipes.copyRecipes(toCopy.recipes);
        return this;
    }

    @Override
    protected void tick(BlockMenu menu, Block b) {
        if (getCharge(menu.getLocation()) < this.energyPerTick) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, NO_ENERGY_ITEM);
            }
            return;
        }

        ItemStack input = menu.getItemInSlot(this.inputSlot[0]);

        if (input == null) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, IDLE_ITEM);
            }
            return;
        }

        ItemStack output = this.recipes.getOutput(input);

        if (output == null) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, IDLE_ITEM);
            }
            return;
        }

        if (AbstractAddon.tickCount() % this.ticksPerOutput == 0) {
            if (menu.fits(output, this.outputSlot)) {
                this.recipes.consumeLastRecipe();
                menu.pushItem(output.clone(), this.outputSlot);
                if (menu.hasViewer()) {
                    menu.replaceExistingItem(this.statusSlot, this.processingItem);
                }
            } else if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, NO_ROOM_ITEM);
                return;
            }
        } else if (menu.hasViewer()) {
            menu.replaceExistingItem(this.statusSlot, this.processingItem);
        }

        removeCharge(menu.getLocation(), this.energyPerTick);
    }

}

