package uwu.smsgamer.pasteclient.modules.modules.render;

import uwu.smsgamer.pasteclient.modules.*;

public class ESP extends Module {
    protected ESP() {
        super("ESP", "Outlines entities", ModuleCategory.RENDER);
        mc.renderGlobal.renderEntityOutlineFramebuffer();
    }
}
