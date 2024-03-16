package com.chyzman.util;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import io.wispforest.endec.Endec;
import io.wispforest.endec.format.json.JsonDeserializer;
import io.wispforest.endec.format.json.JsonSerializer;

public class Identifier implements Comparable<Identifier> {

    public static final Endec<Identifier> ENDEC = Endec.STRING.xmap(Identifier::new, Identifier::toString);

    private static final char SEPARATOR = ':';
    private final String namespace, id;

    public Identifier(String stringified) {
        this(stringified.substring(0, stringified.indexOf(SEPARATOR)), stringified.substring(stringified.indexOf(SEPARATOR) + 1));
    }

    public Identifier(String namespace, String id) {
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
