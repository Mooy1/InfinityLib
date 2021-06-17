package io.github.mooy1.infinitylib.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

final class PathBuilder {

    List<String> path = new ArrayList<>();

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

        String key = line.substring(indent, line.indexOf(':'));
        indent /= 2;

        // change path
        int size = this.path.size();
        if (indent == 0) {
            this.path.clear();
        } else if (indent < size) {
            if (indent + 1 == size) {
                this.path.remove(indent);
            } else {
                this.path.subList(indent, size).clear();
            }
        }
        this.path.add(key);

        return this;
    }

    boolean inMainSection() {
        return this.path.size() == 1;
    }

    @Nonnull
    String build() {
        StringBuilder builder = new StringBuilder();
        if (this.path.size() > 0) {
            builder.append(this.path.get(0));
            for (int i = 1 ; i < this.path.size() ; i++) {
                builder.append('.').append(this.path.get(i));
            }
        }
        return builder.toString();
    }

}
