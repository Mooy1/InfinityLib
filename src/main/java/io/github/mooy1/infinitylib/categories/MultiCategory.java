package io.github.mooy1.infinitylib.categories;

import java.util.Arrays;
import java.util.Comparator;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * A multi category which can hold flex categories and other multi categories
 *
 * @author Mooy1
 */
public final class MultiCategory extends FlexCategory {

    public static final SurvivalSlimefunGuide SURVIVAL_GUIDE = (SurvivalSlimefunGuide)
            SlimefunPlugin.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE);

    private final Category[] subCategories;

    public MultiCategory(NamespacedKey key, ItemStack item, Category... subCategories) {
        this(key, item, 3, subCategories);
    }

    public MultiCategory(NamespacedKey key, ItemStack item, int tier, Category... subCategories) {
        super(key, item, tier);
        Arrays.sort(subCategories, Comparator.comparingInt(Category::getTier));
        this.subCategories = subCategories;
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
        profile.getGuideHistory().add(this, page);

        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "guide.title.main"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), SURVIVAL_GUIDE.getSound(), 1, 1));
        SURVIVAL_GUIDE.createHeader(p, profile, menu);

        menu.addItem(1, new CustomItem(ChestMenuUtils.getBackButton(p, "", ChatColor.GRAY + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.guide"))));
        menu.addMenuClickHandler(1, (pl, s, is, action) -> {
            profile.getGuideHistory().goBack(SURVIVAL_GUIDE);
            return false;
        });

        int index = 9;

        int target = (36 * (page - 1)) - 1;

        while (target < (this.subCategories.length - 1) && index < 45) {
            target++;

            Category category = this.subCategories[target];
            menu.addItem(index, category.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                SlimefunGuide.openCategory(profile, category, mode, 1);
                return false;
            });

            index++;
        }

        int pages = target == this.subCategories.length - 1 ? page : (this.subCategories.length - 1) / 36 + 1;

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
        for (Category category : this.subCategories) {
            if (!category.isRegistered()) {
                category.register(addon);
            }
        }
    }

}
