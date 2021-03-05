package io.github.mooy1.infinitylib.misc;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Util class for blocks
 *
 * @author Seggan
 */
@UtilityClass
public final class BlockUtils {

    private static final Map<TreeType, Set<Material>> treeMats = new EnumMap<>(TreeType.class);

    static {
        treeMats.put(TreeType.TREE, EnumSet.of(Material.OAK_LOG, Material.OAK_LEAVES));
        treeMats.put(TreeType.BIG_TREE, EnumSet.of(Material.OAK_LOG, Material.OAK_LEAVES));
        treeMats.put(TreeType.SWAMP, EnumSet.of(Material.OAK_LOG, Material.OAK_LEAVES, Material.VINE));
        treeMats.put(TreeType.DARK_OAK, EnumSet.of(Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES));

        treeMats.put(TreeType.REDWOOD, EnumSet.of(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES));
        treeMats.put(TreeType.TALL_REDWOOD, EnumSet.of(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES));
        treeMats.put(TreeType.MEGA_REDWOOD, EnumSet.of(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES));

        treeMats.put(TreeType.BIRCH, EnumSet.of(Material.BIRCH_LOG, Material.BIRCH_LEAVES));
        treeMats.put(TreeType.TALL_BIRCH, EnumSet.of(Material.BIRCH_LOG, Material.BIRCH_LEAVES));

        treeMats.put(TreeType.JUNGLE, EnumSet.of(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES));
        treeMats.put(TreeType.SMALL_JUNGLE, EnumSet.of(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES));
        treeMats.put(TreeType.JUNGLE_BUSH, EnumSet.of(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES));
        treeMats.put(TreeType.COCOA_TREE, EnumSet.of(Material.JUNGLE_LOG, Material.JUNGLE_LEAVES, Material.COCOA));

        treeMats.put(TreeType.RED_MUSHROOM, EnumSet.of(Material.MUSHROOM_STEM, Material.RED_MUSHROOM_BLOCK));
        treeMats.put(TreeType.BROWN_MUSHROOM, EnumSet.of(Material.MUSHROOM_STEM, Material.BROWN_MUSHROOM_BLOCK));

        treeMats.put(TreeType.ACACIA, EnumSet.of(Material.ACACIA_LOG, Material.ACACIA_LEAVES));

        treeMats.put(TreeType.CHORUS_PLANT, EnumSet.of(Material.CHORUS_PLANT, Material.CHORUS_FLOWER));

        treeMats.put(TreeType.WARPED_FUNGUS, EnumSet.of(Material.WARPED_STEM, Material.WARPED_WART_BLOCK, Material.TWISTING_VINES_PLANT, Material.SHROOMLIGHT));
        treeMats.put(TreeType.CRIMSON_FUNGUS, EnumSet.of(Material.CRIMSON_STEM, Material.NETHER_WART_BLOCK, Material.WEEPING_VINES_PLANT, Material.SHROOMLIGHT));
    }

    /**
     * This method returns a {@link Set} of blocks connected to each other, as long as those blocks' {@link Material}
     * is in {@code allowedMaterials}
     *
     * @param start the block  to start from
     * @param allowedMaterials all the allowed materials that can connect to the block. if null or empty function returns null
     * @return a set of blocks
     */
    @Nonnull
    public static Set<Block> getNearbyBlocks(@Nonnull Block start, @Nullable Set<Material> allowedMaterials) {
        if (allowedMaterials == null || allowedMaterials.isEmpty()) {
            return new HashSet<>();
        }

        Set<Block> blocks = new HashSet<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block block = start.getLocation().add(x, y, z).getBlock();
                    if (!blocks.contains(block) && allowedMaterials.contains(block.getType())) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
                    }
                }
            }
        }
        return blocks;
    }

    @Nonnull
    private static Set<Block> getNearbyBlocks(@Nonnull Block start, @Nonnull Set<Material> allowedMaterials, @Nonnull Set<Block> blocks) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block block = start.getRelative(x, y, z);
                    if (allowedMaterials.contains(block.getType()) && !blocks.contains(block)) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
                    }
                }
            }
        }
        return blocks;
    }

    /**
     * Returns the blocks of the tree
     *
     * @param start the block to start
     * @param treeType the type of the tree to get
     * @return a set of the blocks contained in the tree
     */
    @Nonnull
    public static Set<Block> getTree(@Nonnull Block start, @Nonnull TreeType treeType) {
        return getNearbyBlocks(start, treeMats.get(treeType));
    }
}
