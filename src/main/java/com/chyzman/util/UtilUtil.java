package com.chyzman.util;

import org.jetbrains.annotations.Nullable;

public class UtilUtil {
    public static <T> @Nullable T thisOr(@Nullable T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}