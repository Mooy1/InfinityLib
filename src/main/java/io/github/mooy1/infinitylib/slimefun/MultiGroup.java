package io.github.mooy1.infinitylib.slimefun;

import java.util.Arrays;
import java.util.Comparator;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.utils.Items;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
/**
 * A multi group which can hold other groups
 *
 * @author Mooy1
 */
@SuppressWarnings("deprecation")
public final class MultiGroup extends FlexItemGroup {

    private final ItemGroup[] subGroups;
    private final String name;

    public MultiGroup(String key, ItemStack item, ItemGroup... subGroups) {
        this(key, item, 3, subGroups);
    }

    public MultiGroup(String key, ItemStack item, int tier, ItemGroup... subGroups) {
        super(AbstractAddon.makeKey(key), item, tier);
        Arrays.sort(subGroups, Comparator.comparingInt(ItemGroup::getTier));
        this.subGroups = subGroups;
        this.name = Items.getName(item);
    }

    @Override
    public boolean isVisible(@Nonnull Player p, @Nonnull PlayerProfile profile, @Nonnull SlimefunGuideMode mode) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        openGuide(p, profile, mode, 1);
    }

    private void openGuide(Player p, PlayerProfile profile, SlimefunGuideMode mode, int page) {
        SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);

        profile.getGuideHistory().add(this, page);

        ChestMenu menu = new ChestMenu(this.name);

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));

        // TODO header

        String back = ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.back.guide");
        menu.addItem(1, ChestMenuUtils.getBackButton(p, "", back));
        menu.addMenuClickHandler(1, (pl, s, is, action) -> {
            profile.getGuideHistory().goBack(guide);
            return false;
        });

        int index = 9;

        int target = (36 * (page - 1)) - 1;

        while (target < (this.subGroups.length - 1) && index < 45) {
            target++;

            ItemGroup category = this.subGroups[target];
            menu.addItem(index, category.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                SlimefunGuide.openCategory(profile, category, mode, 1);
                return false;
            });

            index++;
        }

        int pages = target == this.subGroups.length - 1 ? page : (this.subGroups.length - 1) / 36 + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;
            if (next > 0) {
                openGuide(p, profile, mode, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;
            if (next <= pages) {
                openGuide(p, profile, mode, next);
            }
            return false;
        });

        menu.open(p);
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        super.register(addon);
        for (ItemGroup category : this.subGroups) {
            if (!category.isRegistered()) {
                category.register(addon);
            }
        }
    }

}
