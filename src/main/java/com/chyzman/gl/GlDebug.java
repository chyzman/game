package com.chyzman.gl;

import com.google.common.base.Utf8;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GlDebug {

    private static final Map<Integer, String> GL_MESSAGE_TYPES = Map.of(
            GL_DEBUG_TYPE_MARKER, "MARKER",
            GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR, "DEPRECATED_BEHAVIOR",
            GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR, "UNDEFINED_BEHAVIOR",
            GL_DEBUG_TYPE_ERROR, "ERROR",
            GL_DEBUG_TYPE_OTHER, "OTHER",
            GL_DEBUG_TYPE_PERFORMANCE, "PERFORMANCE",
            GL_DEBUG_TYPE_PORTABILITY, "PORTABILITY",
            GL_DEBUG_TYPE_PUSH_GROUP, "PUSH_GROUP",
            GL_DEBUG_TYPE_POP_GROUP, "POP_GROUP"
    );

    private static final Map<Integer, String> GL_SEVERITIES = Map.of(
            GL_DEBUG_SEVERITY_NOTIFICATION, "NOTIFICATION",
            GL_DEBUG_SEVERITY_LOW, "LOW",
            GL_DEBUG_SEVERITY_MEDIUM, "MEDIUM",
            GL_DEBUG_SEVERITY_HIGH, "HIGH"
    );

    private static int MIN_SEVERITY = GL_DEBUG_SEVERITY_NOTIFICATION;
    private static final Set<Integer> MUTED_TYPES = new HashSet<>();

    public static void muteMessageType(int type) {
        MUTED_TYPES.add(type);
    }

    public static void unmuteMessageType(int type) {
        MUTED_TYPES.remove(type);
    }

    public static void minSeverity(int severity) {
        MIN_SEVERITY = severity;
    }

    public static void attachDebugCallback() {
        glEnable(GL43.GL_DEBUG_OUTPUT);
        glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glDebugMessageCallback(GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            if (severity < MIN_SEVERITY || MUTED_TYPES.contains(type)) return;
            System.out.println("OpenGL Debug Message, type " + GL_MESSAGE_TYPES.get(type) + " severity " + GL_SEVERITIES.get(severity) + ": " + GLDebugMessageCallback.getMessage(length, message));
        }), NULL);
    }
}
