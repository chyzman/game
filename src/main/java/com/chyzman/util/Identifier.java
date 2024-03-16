package com.chyzman.util;

public class Identifier implements Comparable<Identifier> {
    // TODO: Endec
    private static final String SEPARATOR = ":";
    private final String namespace, id;
    public Identifier(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Identifier other)
            return this.namespace.equals(other.namespace) && this.id.equals(other.id);
        return false;
    }

    @Override
    public String toString() {
        return namespace + SEPARATOR + id;
    }

    @Override
    public int compareTo(Identifier o) {
        return 0; // TODO
    }
}
