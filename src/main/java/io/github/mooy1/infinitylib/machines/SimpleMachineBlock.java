package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;
import lombok.experimental.Accessors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.recipes.SimpleRecipes;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public final class SimpleMachineBlock extends TickingMenuBlock implements EnergyNetComponent {

    public static final int[] DEFAULT_INPUT = { 10 };
    public static final int[] DEFAULT_INPUT_BORDER = { 0, 1, 2, 9, 11, 18, 19, 20 };
    public static final int DEFAULT_STATUS = 13;
    public static final int[] DEFAULT_BACKGROUND = { 3, 4, 5, 12, 14, 21, 22, 23 };
    public static final int[] DEFAULT_OUTPUT = { 16 };
    public static final int[] DEFAULT_OUTPUT_BORDER = { 6, 7, 8, 15, 17, 24, 25, 26 };
    private static final ItemStack DEFAULT_PROCESSING_ITEM =
            new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");

    private final SimpleRecipes recipes = new SimpleRecipes();
    private int[] outputBorder = DEFAULT_OUTPUT_BORDER;
    private int[] inputBorder = DEFAULT_INPUT_BORDER;
    private int[] background = DEFAULT_BACKGROUND;
    private int statusSlot = DEFAULT_STATUS;
    private int[] outputSlot = DEFAULT_OUTPUT;
    private int[] inputSlot = DEFAULT_INPUT;
    private ItemStack processingItem = DEFAULT_PROCESSING_ITEM;
    private int ticksPerOutput = 1;
    private int energyPerTick = 10;

    public SimpleMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void setup(MenuBlockPreset preset) {
        preset.drawBackground(MachineItem.OUTPUT_BORDER, this.outputBorder);
        preset.drawBackground(MachineItem.INPUT_BORDER, this.inputBorder);
        preset.drawBackground(MachineItem.BACKGROUND, this.background);
        preset.addBackground(this.statusSlot, MachineItem.IDLE);
    }

    @Override
    protected int[] getInputSlots() {
        return this.inputSlot;
    }

    @Override
    protected int[] getOutputSlots() {
        return this.outputSlot;
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

    @Nonnull
    public SimpleMachineBlock copySlotsFrom(SimpleMachineBlock toCopy) {
        this.background = toCopy.background;
        this.inputSlot = toCopy.inputSlot;
        this.inputBorder = toCopy.inputBorder;
        this.outputSlot = toCopy.outputSlot;
        this.outputBorder = toCopy.outputBorder;
        this.statusSlot = toCopy.statusSlot;
        return this;
    }

    @Override
    protected void tick(BlockMenu menu, Block b) {
        if (getCharge(menu.getLocation()) < this.energyPerTick) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MachineItem.NO_ENERGY);
            }
            return;
        }

        ItemStack input = menu.getItemInSlot(this.inputSlot[0]);

        if (input == null) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MachineItem.IDLE);
            }
            return;
        }

        ItemStack output = this.recipes.getOutput(input);

        if (output == null) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MachineItem.IDLE);
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
                menu.replaceExistingItem(this.statusSlot, MachineItem.NO_ROOM);
                return;
            }
        } else if (menu.hasViewer()) {
            menu.replaceExistingItem(this.statusSlot, this.processingItem);
        }

        removeCharge(menu.getLocation(), this.energyPerTick);
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public final int getCapacity() {
        return this.energyPerTick * 2;
    }

}

