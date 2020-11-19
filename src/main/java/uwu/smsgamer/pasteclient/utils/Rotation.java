package uwu.smsgamer.pasteclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Rotation {
    public final Double yaw;
    public final Double pitch;
    private Double length = -1D;
    private Double lengthSqrd = -1D;

    public Rotation(Number yaw, Number pitch) {
        this.yaw = yaw.doubleValue();
        this.pitch = pitch.doubleValue();
    }

    public static Rotation player() {
        return new Rotation(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch);
    }

    public void toPlayer() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        float yaw = this.yaw.floatValue();
        float pitch = this.pitch.floatValue();
        player.rotationYaw += RotationUtil.angleDiff(yaw, player.rotationYaw);
        player.rotationPitch = pitch;
    }

    public double playerYawDiff() {
        return RotationUtil.angleDiff(Minecraft.getMinecraft().player.rotationYaw, yaw.floatValue());
    }

    public double playerPitchDiff() {
        return RotationUtil.angleDiff(Minecraft.getMinecraft().player.rotationPitch, pitch.floatValue());
    }

    public int pitchToMouse() {
        return RotationUtil.toMouse(pitch);
    }

    public int yawToMouse() {
        return RotationUtil.toMouse(yaw);
    }

    public double getLength() {
        if (lengthSqrd == -1D) lengthSqrd = Math.sqrt(getLengthSquared());
        return lengthSqrd;
    }

    public double getLengthSquared() {
        if (length == -1D)
            length = playerYawDiff() * playerYawDiff() + playerPitchDiff() * playerPitchDiff();
        return length;
    }
}
