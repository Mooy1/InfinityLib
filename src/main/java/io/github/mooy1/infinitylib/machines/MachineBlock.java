package io.github.mooy1.infinitylib.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public final class MachineBlock extends AbstractMachineBlock {

    private final List<MachineBlockRecipe> recipes = new ArrayList<>();
    private int ticksPerOutput = -1;

    public MachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
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
    public void preRegister() {
        if (ticksPerOutput == -1) {
            throw new IllegalStateException("You must call .ticksPerOutput() before registering!");
        }
        super.preRegister();
    }

    @Override
    protected boolean process(Block b, BlockMenu menu) {
        if (AbstractAddon.tickCount() % ticksPerOutput != 0) {
            return true;
        }

        Map<String, MachineInput> map = new HashMap<>(2, 1F);
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null) {
                String string = Slimefun.getItemDataService().getItemData(item).orElseGet(() -> item.getType().name());
                map.compute(string, (str, input) -> input == null ? new MachineInput(item) : input.add(item));
            }
        }

        for (MachineBlockRecipe recipe : recipes) {
             if (recipe.check(map)) {
                 if (quickPush(recipe.output.clone(), menu)) {
                     recipe.consume(map);
                     if (menu.hasViewer()) {
                         menu.replaceExistingItem(layout.statusSlot(), PROCESSING_ITEM);
                     }
                     return true;
                 } else {
                     if (menu.hasViewer()) {
                        menu.replaceExistingItem(layout.statusSlot(), NO_ROOM_ITEM);
                     }
                     return false;
                 }
             }
        }

        if (menu.hasViewer()) {
            menu.replaceExistingItem(layout.statusSlot(), IDLE_ITEM);
        }
        return false;
    }

    public MachineBlock addRecipe(ItemStack output, ItemStack... inputs) {
        MachineBlockRecipe recipe = new MachineBlockRecipe(output);
        for (ItemStack item : inputs) {
            recipe.add(item);
        }
        if (recipe.isValid()) {
            recipes.add(recipe);
        }
        return this;
    }

    public MachineBlock subscribe(MachineRecipeType type) {
        type.subscribe((inputs, outputs) -> addRecipe(outputs, inputs));
        return this;
    }

    private boolean quickPush(ItemStack item, BlockMenu menu) {
        int amount = item.getAmount();
        Material type = item.getType();
        PersistentDataContainer container = null;
        boolean hasItemMeta = item.hasItemMeta();

        for (int slot : getOutputSlots()) {
            ItemStack target = menu.getItemInSlot(slot);

            if (target == null) {
                menu.replaceExistingItem(slot, item, false);
                return true;
            } else if (type == target.getType()) {
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
                    } else if (target.hasItemMeta()) {
                        continue;
                    }

                    int push = Math.min(amount, max);
                    target.setAmount(push + targetAmount);

                    if (push == amount) {
                        return true;
                    } else {
                        amount -= push;
                    }
                }
            }
        }

        return amount < item.getAmount();
    }
}
