package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

@ParametersAreNonnullByDefault
public class CraftingBlock extends TickingMenuBlock {

    public static final ItemStack INVALID_RECIPE = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cInvalid Recipe!");
    public static final ItemStack CLICK_TO_CRAFT = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aClick To Craft!");

    @Setter
    protected MachineLayout layout = MachineLayout.CRAFTING_DEFAULT;
    private final List<CraftingBlockRecipe> recipes = new ArrayList<>();

    public CraftingBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    public final CraftingBlock addRecipe(ItemStack output, ItemStack... inputs) {
        if (inputs.length == 0) {
            throw new IllegalArgumentException("Cannot add recipe with no input!");
        }
        CraftingBlockRecipe recipe = new CraftingBlockRecipe(output, inputs);
        recipes.add(recipe);
        return this;
    }

    @Nonnull
    public final CraftingBlock addRecipesFrom(MachineRecipeType recipeType) {
        recipeType.sendRecipesTo((in, out) -> addRecipe(out, in));
        return this;
    }

    @Override
    protected void setup(BlockMenuPreset preset) {
        preset.drawBackground(OUTPUT_BORDER, layout.outputBorder());
        preset.drawBackground(INPUT_BORDER, layout.inputBorder());
        preset.drawBackground(BACKGROUND_ITEM, layout.background());
        preset.addItem(layout.statusSlot(), INVALID_RECIPE, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        menu.addMenuClickHandler(layout.statusSlot(), (player, i, itemStack, clickAction) -> craft(b, menu, player));
    }

    private boolean craft(Block b, BlockMenu menu, Player p) {
        if (canCraft(b, p)) {
            ItemStack[] input = getInput(menu);
            CraftingBlockRecipe recipe = getOutput(b, input);
            if (recipe != null) {
                if (recipe.check(p)) {
                    if (menu.fits(recipe.output, layout.outputSlots())) {
                        ItemStack item = recipe.output.clone();
                        onSuccessfulCraft(b, menu, p, recipe.output);
                        menu.pushItem(item, layout.outputSlots());
                        recipe.consume(input);
                        tick(b, menu);
                    } else {
                        onNoRoom(p);
                    }
                }
            } else {
                onInvalidRecipe(p);
            }
        }

        return false;
    }

    @Override
    protected final int[] getInputSlots(DirtyChestMenu menu, ItemStack input) {
        return new int[0];
    }

    @Override
    protected final int[] getInputSlots() {
        return layout.inputSlots();
    }

    @Override
    protected final int[] getOutputSlots() {
        return layout.outputSlots();
    }

    @Override
    protected final void tick(Block b, BlockMenu menu) {
        if (menu.hasViewer()) {
            menu.replaceExistingItem(layout.statusSlot(), getStatus(b, menu));
        }
    }

    @Nonnull
    final ItemStack[] getInput(BlockMenu menu) {
        int[] slots = layout.inputSlots();
        ItemStack[] input = new ItemStack[slots.length];
        for (int i = 0; i < slots.length; i++) {
            input[i] = menu.getItemInSlot(slots[i]);
        }
        return input;
    }

    @Nullable
    final CraftingBlockRecipe getOutput(Block b, ItemStack[] input) {
        for (CraftingBlockRecipe recipe : recipes) {
            if (recipe.check(input)) {
                return recipe;
            }
        }
        return null;
    }

    @Nonnull
    protected ItemStack getStatus(Block b, BlockMenu menu) {
        if (getOutput(b, getInput(menu)) == null) {
            return INVALID_RECIPE;
        } else {
            return CLICK_TO_CRAFT;
        }
    }

    protected boolean canCraft(Block b, Player p) {
        return true;
    }

    protected void onInvalidRecipe(Player p) {

    }

    protected void onNoRoom(Player p) {

    }

    protected void onSuccessfulCraft(Block b, BlockMenu menu, Player p, ItemStack output) {

    }

}
