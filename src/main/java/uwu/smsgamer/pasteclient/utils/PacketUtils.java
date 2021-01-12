package uwu.smsgamer.pasteclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;

public class PacketUtils {
    public static class CPacketPlayerBuilder extends CPacketPlayer {
        public CPacketPlayerBuilder() {
        }

        public CPacketPlayerBuilder(Entity entity, boolean move, boolean rotation) {
            if (move) {
                this.x = entity.posX;
                this.y = entity.posY;
                this.z = entity.posZ;
            }
            if (rotation) {
                this.yaw = entity.rotationYaw;
                this.pitch = entity.rotationPitch;
            }
            this.onGround = entity.onGround;
        }

        public CPacketPlayerBuilder(CPacketPlayer base) {
            if (base.getClass().equals(PositionRotation.class)) {
                setMove(base);
                setRot(base);
            } else if (base.getClass().equals(Position.class)) {
                setMove(base);
            } else if (base.getClass().equals(Rotation.class)) {
                setRot(base);
            }
            this.onGround = base.isOnGround();
        }

        private void setMove(CPacketPlayer base) {
            this.moving = true;
            this.x = base.getX(0);
            this.y = base.getY(0);
            this.z = base.getZ(0);
        }

        private void setRot(CPacketPlayer base){
            this.rotating = true;
            this.yaw = base.getYaw(0);
            this.pitch = base.getPitch(0);
        }

        public CPacketPlayerBuilder setX(double x) {
            this.x = x;
            this.moving = true;
            return this;
        }

        public CPacketPlayerBuilder setY(double y) {
            this.y = y;
            this.moving = true;
            return this;
        }

        public CPacketPlayerBuilder setZ(double z) {
            this.z = z;
            this.moving = true;
            return this;
        }

        public CPacketPlayerBuilder setYaw(float yaw) {
            this.yaw = yaw;
            this.rotating = true;
            return this;
        }

        public CPacketPlayerBuilder setPitch(float pitch) {
            this.pitch = pitch;
            this.rotating = true;
            return this;
        }

        public CPacketPlayerBuilder setOnGround(boolean onGround) {
            this.onGround = onGround;
            return this;
        }

        public CPacketPlayer build() {
            if (moving) {
                if (rotating) {
                    return new PositionRotation(x, y, z, yaw, pitch, onGround);
                }
                return new Position(x, y, z, onGround);
            } else if (rotating) {
                return new Rotation(yaw, pitch, onGround);
            } else {
                return new CPacketPlayer(onGround);
            }
        }
    }
}
