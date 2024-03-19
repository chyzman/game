package com.chyzman.attachment;

import com.chyzman.util.Id;
import io.wispforest.endec.impl.BuiltInEndecs;

import java.util.UUID;

public class AttachmentTypes {

    public static final AttachmentType<UUID> UUID = AttachmentType.builder(new Id("game:uuid"), java.util.UUID.class)
            .fullSyncing(BuiltInEndecs.UUID)
            .create();
}
