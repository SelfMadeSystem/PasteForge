package uwu.smsgamer.pasteclient.modules.modules.render.esp;

import net.minecraft.entity.Entity;
import uwu.smsgamer.pasteclient.events.Render3DEvent;
import uwu.smsgamer.pasteclient.utils.*;

import java.awt.*;

public class BoxESP extends ESPModule {
    @Override
    public void onRender(Render3DEvent event, Color color) {
        for (Entity e : mc.world.loadedEntityList) if (TargetUtil.isValid(e)) GLUtil.drawAxisAlignedBBRel(e.getEntityBoundingBox(), color);
    }
}
