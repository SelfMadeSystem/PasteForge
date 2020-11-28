package uwu.smsgamer.pasteclient.modules.modules.render.esp;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import uwu.smsgamer.pasteclient.events.Render3DEvent;
import uwu.smsgamer.pasteclient.utils.*;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class CylinderESP extends ESPModule {
    @Override
    public void onRender(Render3DEvent event, Color c) {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        GLUtil.glColor(c);
        for (Entity e : mc.world.loadedEntityList) if (TargetUtil.isValid(e)) esp(e);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public void esp(Entity e) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION);

        // Draw here
        AxisAlignedBB aabb = GLUtil.getAxisAlignedBBRel(e.getEntityBoundingBox());
        Vec3d center = aabb.getCenter();
        double sizeX = aabb.maxX - aabb.minX;
        double sizeZ = aabb.maxZ - aabb.minZ;

        int add = 10;

        for (int i = 0; i < 360; i += add) {
            double outX0 = + MathUtil.cos(i) * sizeX;
            double outX1 = + MathUtil.cos(i + add) * sizeX;
            double outZ0 = + MathUtil.sin(i) * sizeZ;
            double outZ1 = + MathUtil.sin(i + add) * sizeZ;
            bufferBuilder.pos(center.x, aabb.minY, center.z).endVertex();
            bufferBuilder.pos(center.x + outX0, aabb.minY, center.z + outZ0).endVertex();
            bufferBuilder.pos(center.x + outX1, aabb.minY, center.z + outZ1).endVertex();

            bufferBuilder.pos(center.x, aabb.maxY, center.z).endVertex();
            bufferBuilder.pos(center.x + outX0, aabb.maxY, center.z + outZ0).endVertex();
            bufferBuilder.pos(center.x + outX1, aabb.maxY, center.z + outZ1).endVertex();

            bufferBuilder.pos(center.x + outX0, aabb.minY, center.z + outZ0).endVertex();
            bufferBuilder.pos(center.x + outX0, aabb.maxY, center.z + outZ0).endVertex();
            bufferBuilder.pos(center.x + outX1, aabb.maxY, center.z + outZ1).endVertex();

            bufferBuilder.pos(center.x + outX1, aabb.maxY, center.z + outZ1).endVertex();
            bufferBuilder.pos(center.x + outX1, aabb.minY, center.z + outZ1).endVertex();
            bufferBuilder.pos(center.x + outX0, aabb.minY, center.z + outZ0).endVertex();
        }

        // End drawing

        tessellator.draw();
    }
}
