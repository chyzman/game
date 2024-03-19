package com.chyzman.attachment;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface AttachmentTarget {

    @Nullable
    <T> T get(AttachmentType<T> type);

    @Nullable
    default <T> T getOrCreate(AttachmentType<T> type) {
        var data = this.get(type);

        return data != null ? data : type.defaultValue();
    }

    default <T> T getOrThrow(AttachmentType<T> type) {
        var data = this.get(type);

        if(data != null) return data;

        throw new IllegalStateException("Unable to get the given AttachmentType from the given AttachmentTarget. [Type: " + type.identifier().toString() + "]");
    }

    <T> void set(AttachmentType<T> type, T data);

    void remove(AttachmentType<?> type);

    boolean has(AttachmentType<?> type);

    default <T> void modify(AttachmentType<T> type, Function<T, T> function){
        this.set(type, function.apply(this.get(type)));
    }
}
