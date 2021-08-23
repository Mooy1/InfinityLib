package io.github.mooy1.infinitylib.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

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
@ParametersAreNonnullByDefault
public abstract class AbstractMachineBlock extends TickingMenuBlock implements EnergyNetComponent {

    protected static final ItemStack PROCESSING_ITEM = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");
    protected static final ItemStack NO_ENERGY_ITEM = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");
    protected static final ItemStack NO_ROOM_ITEM = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");
    protected static final ItemStack OUTPUT_BORDER = new CustomItemStack(ChestMenuUtils.getOutputSlotTexture(), "&6Output");
    protected static final ItemStack INPUT_BORDER = new CustomItemStack(ChestMenuUtils.getInputSlotTexture(), "&9Input");
    protected static final ItemStack IDLE_ITEM = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&8Idle");
    protected static final ChestMenu.MenuClickHandler EMPTY_CLICK_HANDLER = ChestMenuUtils.getEmptyClickHandler();
    protected static final ItemStack BACKGROUND_ITEM = ChestMenuUtils.getBackground();

    protected MachineLayout layout;
    protected int energyPerTick = -1;
    protected int energyCapacity = -1;

    public AbstractMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void tick(Block b, BlockMenu menu) {
        if (getCharge(menu.getLocation()) < energyPerTick) {
            updateStatus(menu, NO_ENERGY_ITEM);
        }
        else if (process(b, menu)) {
            removeCharge(menu.getLocation(), energyPerTick);
        }
    }

    protected abstract boolean process(Block b, BlockMenu menu);

    @Override
    protected void setup(MenuBlockPreset preset) {
        preset.drawBackground(OUTPUT_BORDER, layout.outputBorder());
        preset.drawBackground(INPUT_BORDER, layout.inputBorder());
        preset.drawBackground(BACKGROUND_ITEM, layout.background());
        preset.addItem(layout.statusSlot(), IDLE_ITEM, EMPTY_CLICK_HANDLER);
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
    public final int getCapacity() {
        return energyCapacity;
    }

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        if (energyPerTick == -1) {
            throw new IllegalStateException("You must call .energyPerTick() before registering!");
        }
        if (energyCapacity == -1) {
            energyCapacity = energyPerTick * 2;
        }
        if (layout == null) {
            layout = MachineLayout.DEFAULT;
        }
        super.register(addon);
    }

    protected final void updateStatus(BlockMenu menu, ItemStack item) {
        int slot = layout.statusSlot();
        if (menu.getItemInSlot(slot).getType() != item.getType()) {
            menu.replaceExistingItem(layout.statusSlot(), item);
        }
    }

    /**
     * Pushes an item into the menu's output slots, returns true if at least 1 item was pushed
     */
    protected final boolean quickPush(ItemStack item, BlockMenu menu) {
        int amount = item.getAmount();
        Material type = item.getType();
        PersistentDataContainer container = null;
        boolean hasItemMeta = item.hasItemMeta();

        for (int slot : getOutputSlots()) {
            ItemStack target = menu.getItemInSlot(slot);

            if (target == null) {
                menu.replaceExistingItem(slot, item, false);
                return true;
            }
            else if (type == target.getType()) {
                int targetAmount = target.getAmount();
                int max = target.getMaxStackSize() - targetAmount;
                if (max > 0) {
                    if (hasItemMeta) {
                        if (target.hasItemMeta()) {
                            if (container == null) {
                                container = item.getItemMeta().getPersistentDataContainer();
                            }
                            PersistentDataContainer other = target.getItemMeta().getPersistentDataContainer();
                            if (!container.equals(other)) {
                                continue;
                            }
                        }
                    }
                    else if (target.hasItemMeta()) {
                        continue;
                    }

                    int push = Math.min(amount, max);
                    target.setAmount(push + targetAmount);

                    if (push == amount) {
                        return true;
                    }
                    else {
                        amount -= push;
                    }
                }
            }
        }

        return amount < item.getAmount();
    }

}
