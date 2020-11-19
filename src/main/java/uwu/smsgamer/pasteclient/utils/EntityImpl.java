package uwu.smsgamer.pasteclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityImpl extends Entity {
    public EntityImpl(World world) {
        super(world);
    }

    public EntityImpl(World world, double x, double y, double z, double prevX, double prevY, double prevZ, float yaw, float pitch, float prevYaw, float prevPitch) {
        super(world);
        posX = x;
        posY = y;
        posZ = z;
        prevPosX = prevX;
        prevPosY = prevY;
        prevPosZ = prevZ;
        rotationYaw = yaw;
        rotationPitch = pitch;
        prevRotationYaw = prevYaw;
        prevRotationPitch = prevPitch;
    }

    public EntityImpl(World world, Entity entity, float yaw, float pitch, float prevYaw, float prevPitch) {
        super(world);
        posX = entity.posX;
        posY = entity.posY;
        posZ = entity.posZ;
        prevPosX = entity.prevPosX;
        prevPosY = entity.prevPosY;
        prevPosZ = entity.prevPosZ;
        rotationYaw = yaw;
        rotationPitch = pitch;
        prevRotationYaw = prevYaw;
        prevRotationPitch = prevPitch;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
    }
}
