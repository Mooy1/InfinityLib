# InfinityLib
A shaded library for slimefun addons that adds a bunch of useful stuff.

## How to Use
First you need to add InfinityLib to the `dependencies` section in your `pom.xml`:

```xml 
<dependency>
    <groupId>io.github.mooy1</groupId>
    <artifactId>InfinityLib</artifactId>
    <version>SEVEN CHARACTER COMMIT HERE</version>
    <scope>compile</scope>
</dependency>
```

Then you need to relocate it into your own package so that it doesn't conflict with other addon's classes.

Under your `build` section in your `pom.xml`, you should have the following:

```xml
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.1.0</version>
        <configuration>
            <relocations>
                <!-- This is the relocation, make sure to replace the package name -->
                <relocation>
                    <pattern>io.github.mooy1.infinitylib</pattern>
                    <shadedPattern>YOUR.MAIN.PACKAGE.NAME.infinitylib</shadedPattern>
                </relocation>
            </relocations>
            <filters>
                <filter>
                    <artifact>*:*</artifact>
                    <excludes>
                        <exclude>META-INF/*</exclude>
                    </excludes>
                </filter>
                <filter>
                    <artifact>io.github.mooy1:InfinityLib</artifact>
                    <excludes>
                        <!-- Add unneeded packages here -->
                        <exclude>**/infinitylib/EXAMPLE/**</exclude>
                    </excludes>
                </filter>
            </filters>
        </configuration>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

# AbstractAddon
You should extend `AbstractAddon` in your main class.

It adds multiple utility methods as well as implementing auto updating, bstats, and some boilerplate configuration stuff.

You should have a `private static` field for the instance of your plugin.

Your instance field should have a short `public static` getter, something like `inst()` to access the utility methods from anywhere

# Packages

## BStats
BStats is shaded into this library, so no need to shade it yourself.

## Commands
The `CommandUtils` class allows you to easily add commands and tab completion for them.
It also adds a help and info command for you.

Command functionality is created with the class `AbstractCommand`.

## Configuration
The `AddonConfig` class improves on some other config implementations by allowing for comments to be saved.
It can only be used for configs with a default config in your `resources` folder.

## Items
`StackUtils` contains many static utility methods for modifying and reading `ItemStack`s.

## Persistence
Implementation of a few new `PersistantDataType`s including `Block`s, `ItemStack`s, and `ItemStack` Arrays.

`PersistenceUtils` contains all the instances.

## Players
Utility classes relating to players.

`CoolDownMap` is an implementation of a `HashMap<UUID, Long>` which makes it easier to have cooldowns on items.

## Slimefun
Contains a few SlimefunItem implementations to make it easier to create blocks with inventories and tickers.

## Presets
Utility methods for making lore and menus.

## Categories
Slimefun Category classes like a better `MultiCategory` implementation.

# Recipes
Contains `RecipeMap`, `RecipeOutput`, and a `ShapelessRecipe` and `ShapedRecipe` to be used in the `RecipeMap`.
`RecipeMap` is a fast way to do recipe lookup rather than looping and checking every recipe.
