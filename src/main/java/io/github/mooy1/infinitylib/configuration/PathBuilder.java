package io.github.mooy1.infinitylib.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

final class PathBuilder {

    private final List<Integer> dots = new ArrayList<>();
    private StringBuilder path = new StringBuilder();

    @Nonnull
    PathBuilder append(@Nonnull String line) {
        // count indent
        int indent = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                indent++;
            } else {
                break;
            }
        }

        String key = line.substring(indent, line.lastIndexOf(':'));
        indent >>= 1;

        // change path
        if (indent == 0) {
            this.path = new StringBuilder(key);
            if (this.dots.size() != 0) {
                this.dots.clear();
            }
        } else if (indent <= this.dots.size()) {
            this.path.replace(this.dots.get(indent - 1), this.path.length(), key);
        } else {
            this.path.append('.');
            this.dots.add(this.path.length());
            this.path.append(key);
        }

        return this;
    }

    @Nonnull
    String build() {
        return this.path.toString();
    }

}
