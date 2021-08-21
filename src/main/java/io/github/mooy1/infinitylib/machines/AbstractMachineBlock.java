package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;
import lombok.experimental.Accessors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public abstract class AbstractMachineBlock extends TickingMenuBlock implements EnergyNetComponent {

    protected static final ItemStack DEFAULT_PROCESSING_ITEM = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");
    protected static final ItemStack NO_ENERGY_ITEM = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");
    protected static final ItemStack NO_ROOM_ITEM = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");
    protected static final ItemStack OUTPUT_BORDER = new CustomItemStack(ChestMenuUtils.getOutputSlotTexture(), "&6Output");
    protected static final ItemStack INPUT_BORDER = new CustomItemStack(ChestMenuUtils.getInputSlotTexture(), "&9Input");
    protected static final ItemStack IDLE_ITEM = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&8Idle");
    protected static final ChestMenu.MenuClickHandler EMPTY_CLICK_HANDLER = ChestMenuUtils.getEmptyClickHandler();
    protected static final ItemStack BACKGROUND_ITEM = ChestMenuUtils.getBackground();

    protected MachineLayout layout;
    protected ItemStack processingItem;
    protected int energyPerTick = -1;
    protected int energyCapacity = -1;

    public AbstractMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void tick(Block b, BlockMenu menu) {
        if (getCharge(menu.getLocation()) < this.energyPerTick) {
            if (menu.hasViewer()) {
                menu.replaceExistingItem(this.layout.statusSlot(), NO_ENERGY_ITEM);
            }
        } else if (process(b, menu)) {
            removeCharge(menu.getLocation(), this.energyPerTick);
        }
    }

    protected abstract boolean process(Block b, BlockMenu menu);

    @Override
    protected void setup(MenuBlockPreset preset) {
        preset.drawBackground(OUTPUT_BORDER, this.layout.outputBorder());
        preset.drawBackground(INPUT_BORDER, this.layout.inputBorder());
        preset.drawBackground(BACKGROUND_ITEM, this.layout.background());
        preset.addItem(this.layout.statusSlot(), IDLE_ITEM, EMPTY_CLICK_HANDLER);
    }

    @Override
    protected final int[] getInputSlots() {
        return this.layout.inputSlots();
    }

    @Override
    protected final int[] getOutputSlots() {
        return this.layout.outputSlots();
    }

    @Override
    public final int getCapacity() {
        return this.energyCapacity;
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        // TODO impl
    }

}
