package com.chyzman.util;

import io.wispforest.endec.Endec;

import java.nio.file.Path;

public class Id implements Comparable<Id> {

    public static final Endec<Id> ENDEC = Endec.STRING.xmap(Id::new, Id::toString);

    private static final char SEPARATOR = ':';
    private final String namespace, path;

    public Id(String stringified) {
        this(stringified.substring(0, stringified.indexOf(SEPARATOR)), stringified.substring(stringified.indexOf(SEPARATOR) + 1));
    }

    public Id(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String namespace() {
        return this.namespace;
    }

    public String path() {
        return this.path;
    }

    public Path toResourcePath(String type) {
        return Path.of("src/main/resources", this.namespace, type, this.path);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Id other)
            return this.namespace.equals(other.namespace) && this.path.equals(other.path);
        return false;
    }

    @Override
    public String toString() {
        return this.namespace + SEPARATOR + this.path;
    }

    public int compareTo(Id identifier) {
        int result = this.path.compareTo(identifier.path);
        if (result == 0) {
            result = this.namespace.compareTo(identifier.namespace);
        }

        return result;
    }
}
