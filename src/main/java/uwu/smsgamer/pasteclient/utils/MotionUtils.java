package uwu.smsgamer.pasteclient.utils;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.event.ForgeEventFactory;
import uwu.smsgamer.pasteclient.injection.interfaces.*;

import javax.annotation.Nullable;
import java.util.List;

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
        ((IMixinTimer) ((IMixinMinecraft) mc).getTimer()).setTimerSpeed(timer);
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
        ((IMixinEntityPlayer) mc.player).setAirSpeed(amount);
    }

    public static void vset(double amount) {
        mc.player.motionY = amount;
    }

    public static double getFloor() {
        for (int i = 0; i < mc.player.posY; i++) {
            List<AxisAlignedBB> list = Lists.newArrayList();
            if (!getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -i, 0), false, list)) continue;
            double max = -1;
            for (AxisAlignedBB box : list) max = Math.max(box.maxY, max);
            if (max > -1) return Math.min(mc.player.posY, max);
        }
        return 0;
    }

    public static boolean getCollisionBoxes(@Nullable Entity entity, AxisAlignedBB aabb, boolean $idk$, List<AxisAlignedBB> list) {
        int i = MathHelper.floor(aabb.minX) - 1;
        int j = MathHelper.ceil(aabb.maxX) + 1;
        int k = MathHelper.floor(aabb.minY) - 1;
        int l = MathHelper.ceil(aabb.maxY) + 1;
        int i1 = MathHelper.floor(aabb.minZ) - 1;
        int j1 = MathHelper.ceil(aabb.maxZ) + 1;
        World world = mc.world;
        WorldBorder border = world.getWorldBorder();
        boolean flag = entity != null && entity.isOutsideBorder();
        boolean flag1 = entity != null && world.isInsideWorldBorder(entity);
        IBlockState state = Blocks.STONE.getDefaultState();
        BlockPos.PooledMutableBlockPos pmbp = BlockPos.PooledMutableBlockPos.retain();
        if ($idk$ && !ForgeEventFactory.gatherCollisionBoxes(world, entity, aabb, list)) {
            return true;
        } else {
            try {
                for(int k1 = i; k1 < j; ++k1) {
                    for(int l1 = i1; l1 < j1; ++l1) {
                        boolean flag2 = k1 == i || k1 == j - 1;
                        boolean flag3 = l1 == i1 || l1 == j1 - 1;
                        if ((!flag2 || !flag3) && world.isBlockLoaded(pmbp.setPos(k1, 64, l1))) {
                            for(int i2 = k; i2 < l; ++i2) {
                                if (!flag2 && !flag3 || i2 != l - 1) {
                                    if ($idk$) {
                                        if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000) return true;
                                    } else if (entity != null && flag == flag1) {
                                        entity.setOutsideBorder(!flag1);
                                    }

                                    pmbp.setPos(k1, i2, l1);
                                    IBlockState state1;
                                    if (!$idk$ && !border.contains(pmbp) && flag1) {
                                        state1 = state;
                                    } else {
                                        state1 = world.getBlockState(pmbp);
                                    }

                                    state1.addCollisionBoxToList(world, pmbp, aabb, list, entity, false);
                                    if ($idk$ && !ForgeEventFactory.gatherCollisionBoxes(world, entity, aabb, list)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

                return !list.isEmpty();
            } finally {
                pmbp.release();
            }
        }
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
