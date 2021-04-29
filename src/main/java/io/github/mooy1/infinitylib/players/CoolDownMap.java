package io.github.mooy1.infinitylib.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

public final class CoolDownMap {
    
    private final Map<UUID, Long> map = new HashMap<>();
    
    public boolean check(@Nonnull UUID uuid, long cd) {
        return System.currentTimeMillis() - this.map.getOrDefault(uuid, 0L) >= cd;
    }

    public void put(@Nonnull UUID uuid) {
        this.map.put(uuid, System.currentTimeMillis());
    }

    public boolean checkAndPut(@Nonnull UUID uuid, long cd) {
        boolean check = check(uuid, cd);
        if (check) {
            put(uuid);
        }
        return check;
    }
    
}
