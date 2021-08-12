# InfinityLib
A shaded library for slimefun addons that adds a bunch of useful stuff.

# Features


# Usage
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