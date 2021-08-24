# InfinityLib
A shaded library for slimefun addons that adds a bunch of useful stuff.

# Packages & Features

## Core
AbstractAddon is an implementation of JavaPlugin
which you will need to extend for many of the other features to work.
It provides multiple utility methods and does some basic setup for you.

AddonConfig is an implementation of YamlConfiguration
which makes comments available in the user's config
and provides utility methods such as getting a value from within a range
and removing unused/old keys from the user's config.

## Common
CoolDowns is a utility object for keeping track of cool downs of players/uuids

CustomDataType contains some PersistentDataTypes for
ItemStack's, ItemStack Array's, Locations, and String Arrays.
It also provides a constructor for PersistentDataType that uses lambda parameters.

Event contains static utility methods for registering listeners, creating handlers, and calling events

Scheduler provides static utility methods for running and repeating tasks

## Commands
AddonCommand allows you to add commands easily with a parent-child structure,
so you could have a command with a sub command which has a sub command.
It also adds some default commands such as an addon info, aliases, and help command.

## Groups
MultiGroup is an implementation of ItemGroup which lets you organize your groups into SubGroups

SubGroup is an ItemGroup that is hidden from the main page, for use in MultiGroup

## Machines
Not yet completed

Usage with Gradle not yet completed.

First you need to add InfinityLib to the `dependencies` section in your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.mooy1</groupId>
    <artifactId>InfinityLib</artifactId>
    <version>RELEASE TAG HERE</version>
    <scope>compile</scope>
</dependency>
```

Then you need to relocate it into your own package so that it doesn't conflict with other addon's classes.

Under the `build` section in your `pom.xml`, you should have the following:

```xml
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.1.0</version>
        <configuration>
            <!-- This will exclude any unused classes from libraries to reduce your file size -->
            <minimizeJar>true</minimizeJar>
            <relocations>
                <!-- This is the relocation, make sure to replace the package name -->
                <relocation>
                    <pattern>io.github.mooy1.infinitylib</pattern>
                    <shadedPattern>YOUR.MAIN.PACKAGE.HERE.infinitylib</shadedPattern>
                </relocation>
            </relocations>
            <filters>
                <filter>
                    <artifact>*:*</artifact>
                    <excludes>
                        <exclude>META-INF/*</exclude>
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