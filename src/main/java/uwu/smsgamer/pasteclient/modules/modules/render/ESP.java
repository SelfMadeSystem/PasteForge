package uwu.smsgamer.pasteclient.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.*;
import uwu.smsgamer.pasteclient.events.Render3DEvent;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.modules.modules.render.esp.*;
import uwu.smsgamer.pasteclient.utils.StringHashMap;
import uwu.smsgamer.pasteclient.values.*;

import java.awt.*;
import java.util.ArrayList;

public class ESP extends Module {
    private static ESP instance;

    public static ESP getInstance() {
        if (instance == null) throw new Error("???");
        return instance;
    }

    public ChoiceValue<ESPModule> mode;
    public FancyColorValue color = (FancyColorValue) addValue(new FancyColorValue("Color", "Color for ESP.", new Color(255, 0, 0, 64)));
    public NumberValue lineWidth = addDeci("LineWidth", "Width of the line", 1, 0.01, 4, 0.01);

    public ESP() {
        super("ESP", "Outlines entities", ModuleCategory.RENDER);
        if (instance != null) throw new RuntimeException("tf");
        instance = this;
        //noinspection unchecked
        this.mode = (ChoiceValue<ESPModule>) addValue(new ChoiceValue<ESPModule>("Mode", "Mode for ESP.",
          BoxESP.getInstance(), new StringHashMap<>(BoxESP.getInstance(), "Box",
          new HitBoxESP(), "HitBox",
          new OutlineBoxESP(), "OutlineBox",
          new CylinderESP(), "Cylinder",
          new OutlinedCylinderESP(), "Cylinder Outlined",
          BoxESP.getInstance(), "Outline",
          new DickESP(), "Dick")));
    }

    @EventTarget
    private void onRender(Render3DEvent event) {
        if (!getState()) return;
        ESPModule m = mode.getValue();
        if (m != null) m.onRender(event, color.getColor());
    }
}
