package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.StackUtils;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@ParametersAreNonnullByDefault
public final class MachineBlock extends AbstractMachineBlock {

    @Setter
    protected MachineLayout layout = MachineLayout.MACHINE_DEFAULT;
    private final List<MachineBlockRecipe> recipes = new ArrayList<>();
    private int ticksPerOutput = -1;

    public MachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    public MachineBlock addRecipe(ItemStack output, ItemStack... inputs) {
        if (inputs.length == 0) {
            throw new IllegalArgumentException("Cannot add recipe with no input!");
        }
        MachineBlockRecipe recipe = new MachineBlockRecipe(output, inputs);
        recipes.add(recipe);
        return this;
    }

    @Nonnull
    public MachineBlock addRecipesFrom(MachineRecipeType recipeType) {
        recipeType.sendRecipesTo((in, out) -> addRecipe(out, in));
        return this;
    }

    @Nonnull
    public MachineBlock ticksPerOutput(int ticks) {
        if (ticks < 1) {
            throw new IllegalArgumentException("Ticks Per Output must be at least 1!");
        }
        ticksPerOutput = ticks;
        return this;
    }

    @Override
    protected void setup(BlockMenuPreset preset) {
        preset.drawBackground(OUTPUT_BORDER, layout.outputBorder());
        preset.drawBackground(INPUT_BORDER, layout.inputBorder());
        preset.drawBackground(BACKGROUND_ITEM, layout.background());
        preset.addItem(layout.statusSlot(), IDLE_ITEM, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected int[] getInputSlots() {
        return layout.inputSlots();
    }

    @Override
    protected int[] getOutputSlots() {
        return layout.outputSlots();
    }

    @Override
    public void preRegister() {
        if (ticksPerOutput == -1) {
            throw new IllegalStateException("You must call .ticksPerOutput() before registering!");
        }
        super.preRegister();
    }

    @Override
    protected boolean process(Block b, BlockMenu menu) {
        if (AbstractAddon.slimefunTickCount() % ticksPerOutput != 0) {
            return true;
        }

        int[] slots = layout.inputSlots();
        ItemStack[] input = new ItemStack[slots.length];
        for (int i = 0; i < slots.length; i++) {
            input[i] = menu.getItemInSlot(slots[i]);
        }

        MachineBlockRecipe recipe = getOutput(input);
        if (recipe != null) {
            ItemStack rem = menu.pushItem(recipe.output.clone(), layout.outputSlots());
            if (rem == null || rem.getAmount() < recipe.output.getAmount()) {
                recipe.consume();
                if (menu.hasViewer()) {
                    menu.replaceExistingItem(getStatusSlot(), PROCESSING_ITEM);
                }
                return true;
            }
            else {
                if (menu.hasViewer()) {
                    menu.replaceExistingItem(getStatusSlot(), NO_ROOM_ITEM);
                }
                return false;
            }
        }

        if (menu.hasViewer()) {
            menu.replaceExistingItem(getStatusSlot(), IDLE_ITEM);
        }
        return false;
    }

    @Nullable
    MachineBlockRecipe getOutput(ItemStack[] items) {
        Map<String, MachineInput> map = new HashMap<>(2, 1F);
        for (ItemStack item : items) {
            if (item != null) {
                String string = StackUtils.getId(item);
                if (string == null) {
                    string = item.getType().name();
                }
                map.compute(string, (str, input) -> input == null ? new MachineInput(item) : input.add(item));
            }
        }

        for (MachineBlockRecipe recipe : recipes) {
            if (recipe.check(map)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected int getStatusSlot() {
        return layout.statusSlot();
    }

    /**
     * Returns an unmodifiable version of {@link MachineBlock#recipes}
     * @return {@link List}
     */
    @Nonnull
    public List<MachineBlockRecipe> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }
}
