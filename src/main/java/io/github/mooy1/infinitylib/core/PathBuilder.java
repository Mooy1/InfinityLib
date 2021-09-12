package io.github.mooy1.infinitylib.core;

import java.util.ArrayList;
import java.util.List;

final class PathBuilder {

    List<String> path = new ArrayList<>();

    PathBuilder append(String line) {
        // count indent
        int indent = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                indent++;
            }
            else {
                break;
            }
        }

        String key = line.substring(indent, line.indexOf(':'));
        indent /= 2;

        // change path
        int size = path.size();
        if (indent == 0) {
            path.clear();
        }
        else if (indent < size) {
            if (indent + 1 == size) {
                path.remove(indent);
            }
            else {
                path.subList(indent, size).clear();
            }
        }
        path.add(key);

        return this;
    }

    boolean inMainSection() {
        return path.size() == 1;
    }

    String build() {
        StringBuilder builder = new StringBuilder();
        if (path.size() > 0) {
            builder.append(path.get(0));
            for (int i = 1; i < path.size(); i++) {
                builder.append('.').append(path.get(i));
            }
        }
        return builder.toString();
    }

}
