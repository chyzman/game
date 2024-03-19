package com.chyzman.attachment;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AttachmentTargetImpl implements AttachmentTarget {

    private final Map<AttachmentType<?>, Object> attachments = new HashMap<>();

    @Nullable
    public <T> T get(AttachmentType<T> type){
        return (T) attachments.get(type);
    }

    public <T> void set(AttachmentType<T> type, T data){
        this.attachments.put(type, data);
    }

    @Override
    public void remove(AttachmentType<?> type) {
        this.attachments.remove(type);
    }

    public boolean has(AttachmentType<?> type){
        return this.attachments.containsKey(type);
    }
}
