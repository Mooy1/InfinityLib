package io.github.mooy1.infinitylib.mobs;

import io.github.mooy1.infinitylib.core.PluginUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractMob {
    
    private static final NamespacedKey KEY = PluginUtils.getKey("mob");
    
    private static final Map<String, AbstractMob> MOBS = new HashMap<>();
    
    private final String id;
    private final String name;
    private final EntityType type;
    private final int health;
    
    public AbstractMob(@Nonnull String id, @Nonnull String name, @Nonnull EntityType type, int health) {
        Validate.notNull(this.id = id);
        Validate.notNull(this.name = ChatColors.color(name));
        Validate.notNull(this.type = type);
        this.health = health;

        MOBS.put(id, this);
    }
    
    public final LivingEntity spawn(@Nonnull Location l, @Nonnull World world) {
        LivingEntity entity = (LivingEntity) world.spawnEntity(l, this.type);
        
        entity.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, this.id);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(this.health);
        entity.setHealth(this.health);
        entity.setCustomName(this.name);
        entity.setCustomNameVisible(customNameVisible());
        entity.setRemoveWhenFarAway(removeWhenFarAway());
        
        onSpawn(entity);
        
        return entity;
    }
    
    protected boolean customNameVisible() {
        return true;
    }
    
    protected boolean removeWhenFarAway() {
        return true;
    }

    protected void onSpawn(LivingEntity spawned) { }

    protected void onDeath(EntityDeathEvent e) { }

    protected void onPlayerInteract(PlayerInteractEntityEvent e) { }

    protected void onTarget(EntityTargetEvent e) { }

    protected void onSpellCast(EntitySpellCastEvent e) { }

    protected void onDamaged(EntityDamageEvent e) { }

    protected void onAttack(EntityDamageByEntityEvent e) { }
    
    private static AbstractMob get(Entity entity) {
        return MOBS.get(entity.getPersistentDataContainer().get(KEY, PersistentDataType.STRING));
    }
    
    static {
        PluginUtils.registerListener(new Listener() {

            @EventHandler
            public void onTarget(@Nonnull EntityTargetEvent e) {
                AbstractMob mob = get(e.getEntity());
                if (mob != null) {
                    mob.onTarget(e);
                }
            }

            @EventHandler
            public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent e) {
                AbstractMob mob = get(e.getRightClicked());
                if (mob != null) {
                    mob.onPlayerInteract(e);
                }
            }

            @EventHandler
            public void onDeath(@Nonnull EntityDeathEvent e) {
                AbstractMob mob = get(e.getEntity());
                if (mob != null) {
                    mob.onDeath(e);
                }
            }

            @EventHandler
            public void onSpellCast(@Nonnull EntitySpellCastEvent e) {
                AbstractMob mob = get(e.getEntity());
                if (mob != null) {
                    mob.onSpellCast(e);
                }
            }

            @EventHandler
            public void onDamaged(@Nonnull EntityDamageEvent e) {
                AbstractMob mob = get(e.getEntity());
                if (mob != null) {
                    mob.onDamaged(e);
                }
            }
            
            @EventHandler
            public void onAttack(@Nonnull EntityDamageByEntityEvent e) {
                AbstractMob mob = get(e.getDamager());
                if (mob != null) {
                    mob.onAttack(e);
                }
            }
        });
    }
    
}
