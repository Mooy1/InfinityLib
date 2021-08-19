package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;
import lombok.experimental.Accessors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public final class MachineBuilder {

    public static final ItemStack NO_ENERGY_ITEM = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");
    public static final ItemStack NO_ROOM_ITEM = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");
    public static final ItemStack INPUT_BORDER = new CustomItemStack(ChestMenuUtils.getInputSlotTexture(), "&9Input");
    public static final ItemStack OUTPUT_BORDER = new CustomItemStack(ChestMenuUtils.getOutputSlotTexture(), "&6Output");
    public static final ItemStack IDLE_ITEM = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&8Idle");
    public static final ItemStack BACKGROUND_ITEM = ChestMenuUtils.getBackground();
    public static final ChestMenu.MenuClickHandler EMPTY_CLICK_HANDLER = ChestMenuUtils.getEmptyClickHandler();
    public static final ItemStack DEFAULT_PROCESSING_ITEM = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");

    private ItemStack processingItem;
    private int[] outputBorder;
    private int[] inputBorder;
    private int[] outputSlots;
    private int[] inputSlots;
    private int[] background;
    private int statusSlot = -1;
    private int ticksPerOutput = -1;
    private int energyPerTick = -1;
    private int energyCapacity = -1;

    @Nonnull
    public MachineBuilder outputBorder(int... slots) {
        this.outputBorder = slots;
        return this;
    }

    @Nonnull
    public MachineBuilder inputBorder(int... slots) {
        this.inputBorder = slots;
        return this;
    }

    @Nonnull
    public MachineBuilder outputSlots(int... slots) {
        this.outputSlots = slots;
        return this;
    }

    @Nonnull
    public MachineBuilder inputSlots(int... slots) {
        this.inputSlots = slots;
        return this;
    }

    @Nonnull
    public MachineBuilder background(int... slots) {
        this.background = slots;
        return this;
    }

    @Nonnull
    public AbstractMachine register(@Nonnull SlimefunAddon addon) {

    }

}
