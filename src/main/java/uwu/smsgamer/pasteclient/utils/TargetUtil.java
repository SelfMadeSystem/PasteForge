package uwu.smsgamer.pasteclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    private final static Minecraft mc = Minecraft.getMinecraft();

    public static boolean players = true;
    public static boolean mobs = true;
    public static boolean animal = true;
    public static boolean other = true;

    public static boolean isValid(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) return false;
        if (entity.equals(mc.player)) return false;
        if (entity instanceof EntityPlayer) return players;
        else if (entity instanceof EntityAnimal) return animal;
        else if (entity instanceof EntityMob) return mobs;
        else return other;
    }

    public static Entity getClosestEntity(double maxRange, double maxAngle) {
        double lowestDistance = maxRange;
        Entity returnEntity = null;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!isValid(entity)) continue;
            double dist = entity.getDistance(mc.player);
            if (dist < lowestDistance && Math.abs(new RotationUtil(entity).getRotation().playerYawDiff()) <= maxAngle) {
                returnEntity = entity;
                lowestDistance = dist;
            }
        }
        return returnEntity;
    }

    public static Entity getLowestHealthEntity(double maxRange, double maxAngle) {
        double lowestHealth = 20000;
        Entity returnEntity = null;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!isValid(entity)) continue;
            double dist = entity.getDistance(mc.player);
            if (((EntityLivingBase) entity).getHealth() < lowestHealth && dist < maxRange &&
              Math.abs(new RotationUtil(entity).getRotation().playerYawDiff()) <= maxAngle) {
                returnEntity = entity;
                lowestHealth = ((EntityLivingBase) entity).getHealth();
            }
        }
        return returnEntity;
    }

    public static Entity getLowestAngleEntity(double maxRange, double maxAngle) {
        double lowestAngle = maxAngle;
        Entity returnEntity = null;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!isValid(entity)) continue;
            double dist = entity.getDistance(mc.player);
            double angle = Math.abs(new RotationUtil(entity).getRotation().playerYawDiff());
            if (dist < maxRange && angle <= lowestAngle) {
                returnEntity = entity;
                lowestAngle = angle;
            }
        }
        return returnEntity;
    }
}
