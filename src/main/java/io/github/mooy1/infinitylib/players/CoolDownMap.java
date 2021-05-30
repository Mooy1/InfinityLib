package io.github.mooy1.infinitylib.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CoolDownMap {

    private final Map<UUID, Long> map = new HashMap<>();
    private final long cd;

    public boolean check(@Nonnull UUID uuid) {
        return System.currentTimeMillis() - this.map.getOrDefault(uuid, 0L) >= this.cd;
    }

    public void reset(@Nonnull UUID uuid) {
        this.map.put(uuid, System.currentTimeMillis());
    }

    public boolean checkAndReset(@Nonnull UUID uuid) {
        boolean check = check(uuid);
        if (check) {
            reset(uuid);
        }
        return check;
    }

}
