package com.chyzman.gl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static org.lwjgl.opengl.GL45.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL45.glVertexAttribPointer;

public class VertexDescriptor<VertexFunction> {

    private final Function<BufferWriter, VertexFunction> builderFactory;
    private final List<VertexAttribute> attributes = new ArrayList<>();
    private int vertexSize = 0;

    public VertexDescriptor(Consumer<AttributeConsumer> attributeSetup, Function<BufferWriter, VertexFunction> builderFactory) {
        this.builderFactory = builderFactory;
        attributeSetup.accept((name, element, count) -> {
            attributes.add(new VertexAttribute(name, element, count, this.vertexSize));
            this.vertexSize += element.size * count;
        });
    }

    public void prepareAttributes(ToIntFunction<String> attributeLookup) {
        for (var attribute : this.attributes) {
            var location = attributeLookup.applyAsInt(attribute.name);

            glEnableVertexAttribArray(location);
            glVertexAttribPointer(
                    location,
                    attribute.count,
                    attribute.element.glType,
                    false,
                    this.vertexSize,
                    attribute.offset
            );
        }
    }

    public VertexFunction createBuilder(BufferWriter buffer) {
        return this.builderFactory.apply(buffer);
    }

    public int vertexSize() {
        return this.vertexSize;
    }

    private record VertexAttribute(String name, VertexElementType element, int count, int offset) {}

    @FunctionalInterface
    public interface AttributeConsumer {
        void attribute(String name, VertexElementType element, int count);
    }
}