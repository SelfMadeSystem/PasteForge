package uwu.smsgamer.pasteclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.math.*;
import java.nio.*;

public class MathUtil {

    public static Vec3d to2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new Vec3d(screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
    }

    public static Vec3d to2D(Vec3d vec) {
        return to2D(vec.x, vec.y, vec.z);
    }

    public static double dirTo(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y1 - y2, x1 - x2));
    }

    public static double distTo(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static double angleDifference(double a, double b) {
        return ((((a - b) % 360D) + 540D) % 360D) - 180D;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundInc(double val, double inc) {
        return Math.round(val * (1d / inc)) / (1d / inc);
    }

    public static double floorInc(double val, double inc) {
        return Math.floor(val * (1d / inc)) / (1d / inc);
    }

    public static int toMouse(Number deg) {
        return toMouse(deg.floatValue());
    }

    public static int toMouse(float deg) {
        float var13 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float var14 = var13 * var13 * var13 * 8.0F;
        return (int) (deg / var14);
    }

    public static float toDeg(int mouse) {
        float var13 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float var14 = var13 * var13 * var13 * 8.0F;
        return mouse * var14;
    }

    public static boolean sameSides(double a, double b) {
        return (a < 0 && b < 0) || (a > 0 && b > 0);
    }

    public static int toSide(double a) {
        return a < 0 ? -1 : (a > 0 ? 1 : 0);
    }

    public static double sin(double deg) {
        return MathHelper.sin((float) Math.toRadians(deg));
    }

    public static double cos(double deg) {
        return MathHelper.cos((float) Math.toRadians(deg));
    }

    public static boolean approxEquals(double a, double b, double e) {
        return Math.abs(a - b) <= e;
    }
}
