package com.chyzman.util;

import io.wispforest.endec.Endec;

public class Id implements Comparable<Id> {

    public static final Endec<Id> ENDEC = Endec.STRING.xmap(Id::new, Id::toString);

    private static final char SEPARATOR = ':';
    private final String namespace, id;

    public Id(String stringified) {
        this(stringified.substring(0, stringified.indexOf(SEPARATOR)), stringified.substring(stringified.indexOf(SEPARATOR) + 1));
    }

    public Id(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public String namespace() {
        return namespace;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Id other)
            return this.namespace.equals(other.namespace) && this.id.equals(other.id);
        return false;
    }

    @Override
    public String toString() {
        return namespace + SEPARATOR + id;
    }

    @Override
    public int compareTo(Id o) {
        return 0; // TODO
    }
}
