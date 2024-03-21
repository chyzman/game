package com.chyzman.util;

public class Mth {

    public static int floor(double value) {
        int casted = (int) value;
        return value < (double) casted ? casted - 1 : casted;
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : Math.min(value, max);
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    public static int lerp(float delta, int start, int end) {
        return start + (int) Math.floor(delta * (float) (end - start));
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static int hsvToRgb(float hue, float saturation, float value) {
        int i = (int) (hue * 6.0F) % 6;
        float f = hue * 6.0F - (float) i;
        float g = value * (1.0F - saturation);
        float h = value * (1.0F - f * saturation);
        float j = value * (1.0F - (1.0F - f) * saturation);
        float red, green, blue;
        switch (i) {
            case 0:
                red = value;
                green = j;
                blue = g;
                break;
            case 1:
                red = h;
                green = value;
                blue = g;
                break;
            case 2:
                red = g;
                green = value;
                blue = j;
                break;
            case 3:
                red = g;
                green = h;
                blue = value;
                break;
            case 4:
                red = j;
                green = g;
                blue = value;
                break;
            case 5:
                red = value;
                green = g;
                blue = h;
                break;
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }

        return clamp((int) (red * 255f), 0, 255) << 16 | clamp((int) (green * 255f), 0, 255) << 8 | clamp((int) (blue * 255f), 0, 255);
    }
}