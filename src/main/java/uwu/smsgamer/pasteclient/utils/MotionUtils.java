package uwu.smsgamer.pasteclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import uwu.smsgamer.pasteclient.injection.interfaces.*;

public class MotionUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void moveAllTypes(double vclip, double hclip, float timer, float airSpeed,
                                    double vadd, double vmult, double hadd, double hmult) {
        vclip(vclip);
        hclip(hclip);
        timer(timer);
        airSpeed(airSpeed);
        vadd(vadd);
        vmult(vmult);
        hadd(hadd);
        hmult(hmult);
    }

    public static void timer(float timer) {
        ((IMixinTimer)((IMixinMinecraft) mc).getTimer()).setTimerSpeed(timer);
    }

    public static void hmult(double amount) {
        mc.player.motionX *= amount;
        mc.player.motionZ *= amount;
    }

    public static void hadd(double amount) {
        final double yaw = getDirection();
        mc.player.motionX += -MathHelper.sin((float) yaw) * amount;
        mc.player.motionZ += MathHelper.cos((float) yaw) * amount;
        /*
        mc.player.motionX += Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0)) * amount;
        mc.player.motionZ += Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0)) * amount;*/
    }

    public static void hclip(double amount) {
        mc.player.setPositionAndUpdate(mc.player.posX += Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0)) * amount, mc.player.posY,
          mc.player.posZ += Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0)) * amount);
    }

    public static void vmult(double amount) {
        mc.player.motionY *= amount;
    }

    public static void vadd(double amount) {
        mc.player.motionY += amount;
    }

    public static void vclip(double amount) {
        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + amount, mc.player.posZ);
    }

    public static void airSpeed(float amount) {
        ((IMixinEntityPlayer)mc.player).setAirSpeed(amount);
    }

    public static void vset(double amount) {
        mc.player.motionY = amount;
    }

    //Liquidbounce skid

    public static double getSpeed() {
        return Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }

    public static double getSpeed(Entity entity) {
        return entity == null ? 0 : Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean isMoving() {
        return mc.player != null && (mc.player.movementInput.moveForward != 0F || mc.player.movementInput.moveStrafe != 0F);
    }

    public static boolean hasMotion() {
        return mc.player.motionX != 0D && mc.player.motionZ != 0D && mc.player.motionY != 0D;
    }

    public static void strafe(final double speed) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc.player.rotationYaw);
        mc.player.setPosition(mc.player.posX + (-Math.sin(yaw) * length), mc.player.posY, mc.player.posZ + (Math.cos(yaw) * length));
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.player.moveForward < 0F)
            forward = -0.5F;
        else if (mc.player.moveForward > 0F)
            forward = 0.5F;

        if (mc.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}
