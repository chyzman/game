package com.chyzman.attachment;

import com.chyzman.util.Id;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Entity;
import io.wispforest.endec.DataToken;
import io.wispforest.endec.Endec;
import io.wispforest.endec.format.edm.EdmElement;
import io.wispforest.endec.format.edm.EdmEndec;
import io.wispforest.endec.format.edm.EdmMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class AttachmentTypeRegistry {

    public static final DataToken<Dominion> DOMINION_DATA_TOKEN = DataToken.create(Dominion.class, "dominion");

    public static final DataToken<Void> NETWORKED = DataToken.create(Void.class, "networked");

    public static final Endec<Entity> ENDEC = Endec.ofToken(DOMINION_DATA_TOKEN,
            (serializer, dominion, entity) -> EdmEndec.MAP.encode(serializer, toMap(EdmElement.wrapMap(new HashMap<>()).asMap(), serializer.has(NETWORKED), entity)),
            (deserializer, dominion) -> fromMap(EdmEndec.MAP.decode(deserializer), deserializer.has(NETWORKED), dominion)
    );

    private static final Map<Id, AttachmentType<?>> ID_TO_TYPES = new HashMap<>();

    public static void registryType(AttachmentType<?> type){
        if(ID_TO_TYPES.containsKey(type.identifier())) {
            throw new IllegalStateException("An existing AttachmentType with the given Id already exists! [Id: " + type.identifier() + "]");
        }

        ID_TO_TYPES.put(type.identifier(), type);
    }

    //--

    private static EdmMap toMap(EdmMap map, boolean networked, Entity entity){
        for (var type : ID_TO_TYPES.values()) {
            toMap(map, networked, entity, type);
        }

        return map;
    }

    private static <T> void toMap(EdmMap map, boolean networked, Entity entity, AttachmentType<T> type) {
        var key = networked ? type.networkEndecKey() : type.endecKey();

        if(key == null) return;

        var data = entity.get(type.clazz());

        if(data == null) return;

        map.put(key, data);
    }

    private static Entity fromMap(EdmMap map, boolean networked, Dominion dominion){
        Map<AttachmentType<?>, Object> attachments = new HashMap<>();

        for (var type : ID_TO_TYPES.values()) {
            var data = fromMap(map, networked, type);

            if(data != null) attachments.put(type, data);
        }

        if(attachments.containsKey(AttachmentTypes.UUID)) {
            var uuid = map.get(AttachmentTypes.UUID.endecKey());

            Entity targetEntity = null;

            for (var result : dominion.findEntitiesWith(UUID.class)) {
                if(result.comp().equals(uuid)) continue;

                targetEntity = result.entity();
            }

            if(targetEntity != null) {
                for (Object value : attachments.values()) {
                    targetEntity.add(value);
                }

                return targetEntity;
            }
        }

        return dominion.createEntity(attachments.values().toArray());
    }

    private static <T> T fromMap(EdmMap map, boolean networked, AttachmentType<T> type) {
        var key = networked ? type.networkEndecKey() : type.endecKey();

        if(key == null) return null;

        var data = map.get(key);

        if(data == null) return null;

        return data;
    }

    //--

    private static Endec<Object> endec(Function<AttachmentType<?>, Endec<?>> func) {
        return Endec.<Object, Id>dispatched(id -> {
            var type = ID_TO_TYPES.get(id);

            if(type == null) {
                throw new IllegalStateException("Unable to locate a given AttachmentType for a passed Id! [Id: " + id + "]");
            }

            return func.apply(type);
        }, o -> {
            if(!(o instanceof AttachedType attachedType)) {
                throw new IllegalStateException("Unable to find the AttachmentType for the given passed object! [Class: " + o.getClass().getSimpleName() + "]");
            }

            return attachedType.getType().identifier();
        }, Id.ENDEC);
    }
}
