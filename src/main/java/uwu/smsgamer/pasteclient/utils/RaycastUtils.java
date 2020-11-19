package uwu.smsgamer.pasteclient.utils;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;

import java.util.List;

public class RaycastUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static RayTraceResult getRayTraceResult(float partialTicks) {
        if (mc.playerController == null) return mc.objectMouseOver;
        return getRayTraceResult(partialTicks, Minecraft.getMinecraft().getRenderViewEntity(), 3, mc.playerController.getBlockReachDistance());
    }

    public static RayTraceResult getRayTraceResult(float partialTicks, Entity viewEntity, double eReach, double bReach) {
        Entity pointedEntity;
        RayTraceResult objectMouseOver = mc.objectMouseOver;
        if (viewEntity != null && mc.world != null) {
            mc.mcProfiler.startSection("pick");
            mc.pointedEntity = null;
            double d0 = bReach;
            objectMouseOver = viewEntity.rayTrace(d0, partialTicks);
            Vec3d vec3d = viewEntity.getPositionEyes(partialTicks);
            boolean flag = false;
            double d1 = d0;
            if (mc.playerController.extendedReach()) {
                d1 = eReach * 2;
                d0 = d1;
            } else if (d0 > eReach) {
                flag = true;
            }

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3d);
            }

            Vec3d vec3d1 = viewEntity.getLook(1.0F);
            Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
            pointedEntity = null;
            Vec3d vec3d3 = null;
            List<Entity> list = mc.world.getEntitiesInAABBexcluding(viewEntity, viewEntity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith()));
            double d2 = d1;

            for (Entity value : list) {
                AxisAlignedBB axisalignedbb = value.getEntityBoundingBox().grow(value.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (axisalignedbb.contains(vec3d)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = value;
                        vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                        d2 = 0.0D;
                    }
                } else if (raytraceresult != null) {
                    double d3 = vec3d.distanceTo(raytraceresult.hitVec);
                    if (d3 < d2 || d2 == 0.0D) {
                        if (value.getLowestRidingEntity() == viewEntity.getLowestRidingEntity() && !value.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = value;
                                vec3d3 = raytraceresult.hitVec;
                            }
                        } else {
                            pointedEntity = value;
                            vec3d3 = raytraceresult.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0D) {
                pointedEntity = null;
                objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mc.pointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();
        }
        return objectMouseOver;
    }
}
