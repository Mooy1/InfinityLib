package io.github.mooy1.infinitylib.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.RequiredArgsConstructor;

/**
 * An object for holding cool downs based on uuids
 *
 * @author Mooy1
 */
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public final class CoolDowns {

    private final Map<UUID, Long> map = new HashMap<>();
    private final long cd;

    public boolean check(UUID uuid) {
        return System.currentTimeMillis() - this.map.getOrDefault(uuid, 0L) >= this.cd;
    }

    public void reset(UUID uuid) {
        this.map.put(uuid, System.currentTimeMillis());
    }

    public boolean checkAndReset(UUID uuid) {
        boolean check = check(uuid);
        if (check) {
            reset(uuid);
        }
        return check;
    }

}
