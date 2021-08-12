package io.github.mooy1.infinitylib.slimefun;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.utils.Items;
import io.github.mooy1.infinitylib.utils.MenuItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@Setter
@ParametersAreNonnullByDefault
public class SimpleMachine extends TickingMenuBlock implements EnergyNetComponent {

    public static final int[] DEFAULT_INPUT = { 10 };
    public static final int[] DEFAULT_INPUT_BORDER = { 0, 1, 2, 9, 11, 18, 19, 20 };
    public static final int DEFAULT_STATUS = 13;
    public static final int[] DEFAULT_STATUS_BORDER = { 3, 4, 5, 12, 14, 21, 22, 23 };
    public static final int[] DEFAULT_OUTPUT = { 16 };
    public static final int[] DEFAULT_OUTPUT_BORDER = { 6, 7, 8, 15, 17, 24, 25, 26 };
    private static final ItemStack DEFAULT_PROCESSING_ITEM =
            new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");

    private final Map<String, Duo<ItemStack, Integer>> recipes = new HashMap<>();
    private int[] statusBorder = DEFAULT_STATUS_BORDER;
    private int[] outputBorder = DEFAULT_OUTPUT_BORDER;
    private int[] inputBorder = DEFAULT_INPUT_BORDER;
    private int[] background = new int[0];
    private int statusSlot = DEFAULT_STATUS;
    private int[] outputSlot = DEFAULT_OUTPUT;
    private int[] inputSlot = DEFAULT_INPUT;
    private ItemStack processingItem = DEFAULT_PROCESSING_ITEM;
    private int ticksPerOutput = 1;
    private int energy = 10;

    public SimpleMachine(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void setup(MenuBlockPreset preset) {
        preset.drawBackground(this.background);
        preset.drawBackground(MenuItem.STATUS_BORDER, this.statusBorder);
        preset.drawBackground(MenuItem.OUTPUT_BORDER, this.outputBorder);
        preset.drawBackground(MenuItem.INPUT_BORDER, this.inputBorder);
        preset.addItem(this.statusSlot, MenuItem.LOADING);
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
    public SimpleMachine addRecipe(ItemStack input, ItemStack output) {
        this.recipes.put(Items.getId(input), new Duo<>(output, input.getAmount()));
        return this;
    }

    @Nonnull
    public SimpleMachine copyRecipesFrom(SimpleMachine toCopy) {
        this.recipes.putAll(toCopy.recipes);
        return this;
    }

    @Nonnull
    public SimpleMachine copySlotsFrom(SimpleMachine toCopy) {
        this.background = toCopy.background;
        this.inputSlot = toCopy.inputSlot;
        this.inputBorder = toCopy.inputBorder;
        this.outputSlot = toCopy.outputSlot;
        this.outputBorder = toCopy.outputBorder;
        this.statusSlot = toCopy.statusSlot;
        this.statusBorder = toCopy.statusBorder;
        return this;
    }

    @Override
    protected void tick(@Nonnull BlockMenu menu, @Nonnull Block b) {
        if (getCharge(menu.getLocation()) < this.energy) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MenuItem.NO_ENERGY);
            }
            return;
        }

        ItemStack input = menu.getItemInSlot(this.inputSlot[0]);

        if (input == null) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MenuItem.NO_INPUT);
            }
            return;
        }

        Duo<ItemStack, Integer> output = this.recipes.get(Items.getIdOrType(input));

        if (output == null || input.getAmount() < output.second()) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MenuItem.INVALID_INPUT);
            }
            return;
        }

        if (menu.hasViewer()) {
            menu.replaceExistingItem(this.statusSlot, this.processingItem);
        }

        if (AbstractAddon.tickCount() % this.ticksPerOutput == 0) {
            if (menu.fits(output.first(), this.outputSlot)) {
                input.setAmount(input.getAmount() - output.second());
                menu.pushItem(output.first().clone(), this.outputSlot);
            } else if (menu.hasViewer()) {
                menu.replaceExistingItem(this.statusSlot, MenuItem.NO_ROOM);
                return;
            }
        }

        removeCharge(menu.getLocation(), this.energy);
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public final int getCapacity() {
        return this.energy * 2;
    }

}
