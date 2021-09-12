package io.github.mooy1.infinitylib;

import lombok.experimental.UtilityClass;

/**
 * Contains a filtered string of InfinityLib's version and packages for verifying relocation
 */
@UtilityClass
public final class InfinityLib {

    /**
     * The version of this InfinityLib package, for example 1.2.3 or Unofficial
     */
    public static final String VERSION = "${project.version}";

    /**
     * The package of this class, for example: me.name.addon.infinitylib
     */
    public static final String PACKAGE = InfinityLib.class.getPackage().getName();

    /**
     * The package of the addon that shaded this, for example: me.name.addon
     */
    public static final String ADDON_PACKAGE = PACKAGE.substring(0, PACKAGE.lastIndexOf('.'));

}
