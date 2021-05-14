# InfinityLib
A shaded library for slimefun addons.

## How to Use

## AbstractAddon
You should extend `AbstractAddon` in your main class.

It adds multiple utility methods as well as implementing auto updating, bstats, and some boilerplate configuration stuff.

You should have a `private static` field for the instance of your plugin.
Y
our instance field should have a short `public static` getter, something like `inst()`

Simply call `super.onEnable()` in your `onEnable()` method right after setting your instance.

## BStats
BStats is shaded into this library, so no need to shade it yourself.

## Commands
The `CommandManager` class allows you to easily add commands and tab completion for them.
It also adds a help and info command for you.

Command functionality is created with the class `AbstractCommand`.

## Configuration
The `AddonConfig` class improves on some other config implementations by allowing for comments to be saved.
It will also add an auto update field and comment to the config.yml. 
It can only be used for configs with a default config in your `resources` fold.

## Items
Utility classes for `ItemStack`s.

`StackUtils` contains many static utility methods for modifying and reading `ItemStack`s.

`ShinyEnchant` is a useless enchant implementation which you can use to make items shiny.

## Persistence
Implementation of a few new `PersistantDataType`s including `Block`s, `ItemStack`s, and `ItemStack` Arrays.

`PersistenceUtils` contains all the instances.

## Players
Utility classes relating to players.

`CoolDownMap` is an implementation of a `HashMap<UUID, Long>` which makes it easier to have cooldowns on items.

## Slimefun
Package for utilities that are specifically slimefun related.

### Abstracts
Contains a few SlimefunItem implementations to make it easier to create blocks with inventories and tickers.

### Presets
Utility classes, field, and methods for making lore and menus.

### Utils
Utility classes related to slimefun like a better `MultiCategory` implementation.

### Recipes
Classes that should server as keys and values in `HashMap`s which allows for fast recipe lookup in machines.

#### Inputs
The keys of a recipe `HashMap`.

These implement `equals()` and `hashCode()`.

#### Outputs
The values of a recipe `HashMap`.

These are just holders for an output `ItemStack` and some form of input amount to be used for item consumption.
