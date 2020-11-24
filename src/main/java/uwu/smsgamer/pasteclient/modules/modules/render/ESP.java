package uwu.smsgamer.pasteclient.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import uwu.smsgamer.pasteclient.events.Render3DEvent;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.GLUtil;

import java.awt.*;

public class ESP extends Module {
    public ESP() {
        super("ESP", "Outlines entities", ModuleCategory.RENDER);
        mc.renderGlobal.renderEntityOutlineFramebuffer();
    }

    @EventTarget
    private void onRender(Render3DEvent event) {
        if (!getState()) return;
        for (Entity entity : mc.world.loadedEntityList) render(entity);
    }

    private void render(Entity entity) {
        GLUtil.drawAxisAlignedBBRel(entity.getEntityBoundingBox(), new Color(255, 0, 0, 64));
    }
}
