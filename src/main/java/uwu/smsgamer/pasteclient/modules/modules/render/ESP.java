package uwu.smsgamer.pasteclient.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import uwu.smsgamer.pasteclient.events.Render3DEvent;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.modules.modules.render.esp.*;
import uwu.smsgamer.pasteclient.values.*;

import java.awt.*;
import java.util.ArrayList;

public class ESP extends Module {
    private static ESP instance;

    public static ESP getInstance() {
        if (instance == null) instance = new ESP();
        return instance;
    }

    public IntChoiceValue mode = addIntChoice("Mode", "Mode for ESP.", 0,
      0, "Box",
      1, "HitBox",
      2, "OutlineBox",
      3, "Cylinder",
      4, "Outline",
      5, "Dick");
    public FancyColorValue color = (FancyColorValue) addValue(new FancyColorValue("Color", "Color for ESP.", new Color(255, 0, 0, 64)));
    public NumberValue lineWidth = addDeci("LineWidth", "Width of the line", 1, 0.01, 4, 0.01);

    public ArrayList<ESPModule> esps = new ArrayList<>(5);

    public ESP() {
        super("ESP", "Outlines entities", ModuleCategory.RENDER);
        esps.add(0, new BoxESP());
        esps.add(1, new HitBoxESP());
        esps.add(2, new OutlineBoxESP()); // TODO: 2020-11-27 finish this shit
        esps.add(3, new BoxESP());
        esps.add(4, new BoxESP());
        esps.add(5, DickESP.getInstance());
        addValue(DickESP.getInstance().animation);
    }

    @EventTarget
    private void onRender(Render3DEvent event) {
        if (!getState()) return;
        ESPModule m = esps.get(mode.getValue());
        if (m != null) m.onRender(event, color.getColor());
    }
}
