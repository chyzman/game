package com.chyzman.attachment;

import com.chyzman.util.Id;
import io.wispforest.endec.Endec;
import io.wispforest.endec.impl.KeyedEndec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record AttachmentType<T>(Id identifier, Class<T> clazz, @Nullable Supplier<T> constructor, @Nullable Endec<T> endec, @Nullable Endec<T> networked, @Nullable KeyedEndec<T> endecKey, @Nullable KeyedEndec<T> networkEndecKey) {

    @Nullable
    public T defaultValue() {
        return (this.constructor != null) ? this.constructor.get() : null;
    }

    public static <T> AttachmentType.Builder<T> builder(Id identifier, Class<T> clazz) {
        return new AttachmentType.Builder<>(identifier, clazz);
    }

    public static class Builder<T> {
        private final Id identifier;
        private final Class<T> clazz;

        @Nullable private Supplier<T> constructor = null;

        @Nullable private Endec<T> endec = null;
        @Nullable private Endec<T> networked = null;

        private Builder(Id identifier, Class<T> clazz) {
            this.identifier = identifier;
            this.clazz = clazz;
        }

        public Builder<T> constructor(Supplier<T> constructor) {
            this.constructor = constructor;

            return this;
        }

        public Builder<T> persistent(Endec<T> endec) {
            this.endec = endec;

            return this;
        }

        public Builder<T> networked(Endec<T> networked) {
            this.networked = networked;

            return this;
        }

        public Builder<T> fullSyncing(Endec<T> endec) {
            this.endec = endec;
            this.networked = endec;

            return this;
        }

        public AttachmentType<T> create() {
            KeyedEndec<T> endecKey = (this.endec != null && this.constructor != null)
                    ? this.endec.keyed(this.identifier.toString(), this.constructor)
                    : null;

            KeyedEndec<T> networkEndecKey = (this.networked != null && this.constructor != null)
                    ? this.networked.keyed(this.identifier.toString(), this.constructor)
                    : null;

            return new AttachmentType<>(this.identifier, this.clazz, this.constructor, this.endec, this.networked, endecKey, networkEndecKey);
        }
    }
}
