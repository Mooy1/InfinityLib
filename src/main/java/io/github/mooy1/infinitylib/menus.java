package io.github.mooy1.infinitylib;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class menus {

    /**
     * Collection of item transfer utils
     * Some are modified methods from CargoUtils
     *
     * @author Mooy1
     */
    public static final class TransferUtils {
        
        /**
         * This method gets the BlockMenu of a location
         *
         * @param location location
         * @return the BlockMenu if it exists
         */
        @Nullable
        public static BlockMenu getMenu(@Nonnull Location location) {
            return BlockStorage.getInventory(location);
        }
    
        /**
         * This method gets the vanilla inventory of a block
         *
         * @param b block
         * @return the inventory if it exists
         */
        @Nullable
        public static Inventory getInventory(@Nonnull Block b) {
            if (hasInventory(b)) {
                
                BlockState state = PaperLib.getBlockState(b, false).getState();
    
                if (state instanceof InventoryHolder) {
    
                    return ((InventoryHolder) state).getInventory();
                }
            }
            return null;
        }
    
        /**
         * This method gets the location of the other chest in a double chest
         *
         * @param inventory inventory of chest
         * @return both locations
         */
        @Nullable
        public static Pair<Location, Location> getBothChests(@Nonnull Inventory inventory) {
            InventoryHolder holder = inventory.getHolder();
    
            if (holder instanceof DoubleChest) {
    
                DoubleChest doubleChest = ((DoubleChest) holder);
                Chest leftChest = (Chest) doubleChest.getLeftSide();
                Chest rightChest = (Chest) doubleChest.getRightSide();
    
                if (leftChest != null && rightChest != null) {
                    return new Pair<>(leftChest.getLocation(), rightChest.getLocation());
                }
            }
    
            return null;
        }
        
        public static boolean testDoubleChest(@Nullable Inventory inv, @Nonnull Location a, @Nonnull Location b) {
            if (inv != null) {
                Pair<Location, Location> pair = getBothChests(inv);
                if (pair == null) return false;
                return pair.getFirstValue().equals(a) && pair.getSecondValue().equals(b) || pair.getSecondValue().equals(a) && pair.getFirstValue().equals(b);
            }
            return false;
        }
    
        /**
         * This methods gets the slots of a BlockMenu for a type of flow
         *
         * @param menu menu to check
         * @param itemTransportFlow input or output
         * @param item item being inputted
         * @return slots
         */
        public static int[] getSlots(@Nonnull BlockMenu menu, @Nonnull ItemTransportFlow itemTransportFlow, @Nullable ItemStack item) {
            return menu.getPreset().getSlotsAccessedByItemTransport(menu, itemTransportFlow, item);
        }
        
        public static boolean hasInventory(@Nonnull Block block) {
    
            Material type = block.getType();
    
            if (SlimefunTag.SHULKER_BOXES.isTagged(type)) {
                return true;
            }
    
            switch (type) {
                case CHEST:
                case TRAPPED_CHEST:
                case FURNACE:
                case DISPENSER:
                case DROPPER:
                case HOPPER:
                case BREWING_STAND:
                case BARREL:
                case BLAST_FURNACE:
                case SMOKER:
                    return true;
                default:
                    return false;
            }
        }
    
        /**
         * This method gets the output slots of a vanilla inventory
         *
         * @param inv inventory
         * @return slot range
         */
        public static int[] getOutputSlots(Inventory inv) {
            if (inv instanceof FurnaceInventory) {
                return new int[]{2};
            } else if (inv instanceof BrewerInventory) {
                return new int[]{0, 1, 2};
            } else {
                int[] array = new int[inv.getSize()];
                for (int i = 0 ; i < inv.getSize() ; i++) {
                    array[i] = i;
                }
                return array;
            }
        }
    
        /**
         * This method gets the input slot range of a vanilla inventory
         *
         * @param inv inventory
         * @param item item being inserted
         * @return slot range
         */
        public static int[] getInputSlots(@Nonnull Inventory inv, @Nullable ItemStack item) {
            if (inv instanceof FurnaceInventory) {
                if (item != null && item.getType().isFuel()) {
                    if (isSmeltable(item)) {
                        // Any non-smeltable items should not land in the upper slot
                        return new int[]{0};
                    } else {
                        return new int[]{1};
                    }
                } else {
                    return new int[]{0};
                }
            } else if (inv instanceof BrewerInventory) {
                if (isPotion(item)) {
                    return new int[]{0, 1, 2};
                } else if (item != null && item.getType() == Material.BLAZE_POWDER) {
                    return new int[]{4};
                } else {
                    return new int[]{3};
                }
            } else {
                int[] array = new int[inv.getSize()];
                for (int i = 0 ; i < inv.getSize() ; i++) {
                    array[i] = i;
                }
                return array;
            }
        }
    
        public static boolean isSmeltable(@Nullable ItemStack stack) {
            return SlimefunPlugin.getMinecraftRecipeService().isSmeltable(stack);
        }
    
        public static boolean isPotion(@Nullable ItemStack item) {
            return item != null && (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION);
        }
    
        /**
         * This method inserts an ItemStack into a vanilla inventory
         *
         * @param stack stack to insert
         * @param inv inventory to insert to
         * @return remaining items
         */
        @Nullable
        public static ItemStack insertToVanillaInventory(@Nonnull ItemStack stack, @Nonnull Inventory inv) {
            ItemStack[] contents = inv.getContents();
    
            ItemStackWrapper wrapper = new ItemStackWrapper(stack);
    
            for (int slot : getInputSlots(inv, stack)) {
                // Changes to this ItemStack are synchronized with the Item in the Inventory
                ItemStack itemInSlot = contents[slot];
    
                if (itemInSlot == null) {
                    inv.setItem(slot, stack);
                    return null;
                } else {
                    int maxStackSize = itemInSlot.getType().getMaxStackSize();
    
                    if (SlimefunUtils.isItemSimilar(itemInSlot, wrapper, true, false) && itemInSlot.getAmount() < maxStackSize) {
                        int amount = itemInSlot.getAmount() + stack.getAmount();
    
                        if (amount > maxStackSize) {
                            stack.setAmount(amount - maxStackSize);
                        } else {
                            stack = null;
                        }
    
                        itemInSlot.setAmount(Math.min(amount, maxStackSize));
                        return stack;
                    }
                }
            }
    
            return stack;
        }
    
        public static ItemStack insertToBlockMenu(@Nonnull BlockMenu menu, @Nonnull ItemStack item) {
            return menu.pushItem(item, TransferUtils.getSlots(menu, ItemTransportFlow.INSERT, item));
        }
        
    }

    /**
     * Collection of locations utils
     *
     * @author Mooy1
     */
    public static final class LocationUtils {
        
        /**
         * This method returns an array of all adjacent locations from a location
         *
         * @param l location
         * @return adjacent locations
         */
        @Nonnull
        public static Location[] getAdjacentLocations(@Nonnull Location l, boolean random) {
            List<Location> locations = new ArrayList<>(6);
            locations.add(l.clone().add(1, 0, 0));
            locations.add(l.clone().add(-1, 0, 0));
            locations.add(l.clone().add(0, 1, 0));
            locations.add(l.clone().add(0, -1, 0));
            locations.add(l.clone().add(0, 0, 1));
            locations.add(l.clone().add(0, 0, -1));
            
            if (random) {
                Collections.shuffle(locations);
            }
    
            return locations.toArray(new Location[6]);
        }
    
        /**
         * This method gets an adjacent location from a location and the direction
         *
         * @param l location
         * @param direction direction which can be found between 2 locations with getDirectionInt
         * @return the location in the direction
         */
        @Nullable
        public static Location getRelativeLocation(@Nonnull Location l, int direction) {
            Location clone = l.clone();
            switch (direction) {
                case 0: return clone.add(1, 0, 0);
                case 1: return clone.add(-1, 0, 0);
                case 2: return clone.add(0, 1, 0);
                case 3: return clone.add(0, -1, 0);
                case 4: return clone.add(0, 0, 1);
                case 5: return clone.add(0, 0, -1);
                default: return null;
            }
        }
    
        /**
         * This method gets the direction number from 2 locations
         *
         * @param current the current location
         * @param target the target location
         * @return the direction int to go from the current to the target
         */
        public static int getDirectionInt(@Nonnull Location current, @Nonnull Location target) {
            int currentX = current.getBlockX();
            int currentY = current.getBlockY();
            int currentZ = current.getBlockZ();
            int targetX = target.getBlockX();
            int targetY = target.getBlockY();
            int targetZ = target.getBlockZ();
    
            if (targetX - currentX == 1) {
                return 0;
            } else if (targetX - currentX == -1) {
                return 1;
            } else if (targetY - currentY == 1) {
                return 2;
            } else if (targetY - currentY == -1) {
                return 3;
            } else if (targetZ - currentZ == 1) {
                return 4;
            } else if (targetZ - currentZ == -1) {
                return 5;
            } else {
                return -1;
            }
        }
        
    }

}
