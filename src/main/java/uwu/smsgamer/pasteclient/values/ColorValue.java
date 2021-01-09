package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import uwu.smsgamer.pasteclient.utils.Colour;

import java.awt.*;

public class ColorValue extends Value<Color> {
    private Colour.RGB rgb;
    private Colour.HSV hsv;
    private double alpha;
    public ColorValue(String name, String description, Color dVal) {
        super(name, description, null);
        setColor(dVal);
    }

    public ColorValue(String name, String description, Color dVal, Value<?>... children) {
        super(name, description, null, children);
        setColor(dVal);
    }

    public Color getColor() {
        return rgb.toColor();
    }

    public Colour.RGB getRGB() {
        return rgb;
    }

    public Colour.HSV getHSV() {
        return hsv;
    }

    public double getAlpha() {
        return alpha;
    }

    @Override
    public Color getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.setColor(new Color(((Number) value).intValue()));
        } else if (value instanceof Color) {
            this.setColor((Color) value);
        } else if (value instanceof Colour.RGB) {
            this.setRGB((Colour.RGB) value);
        } else if (value instanceof Colour.HSV) {
            this.setHSV((Colour.HSV) value);
        } else {
            throw new IllegalArgumentException("Value is not acceptable: " + value.toString());
        }
    }

    public void setColor(Color color) {
        this.rgb = new Colour.RGB(color);
        this.hsv = Colour.rgb2hsv(this.rgb);
        this.alpha = color.getAlpha()/255F;
    }

    public void setRGB(Colour.RGB rgb) {
        this.rgb = rgb;
        this.hsv = Colour.rgb2hsv(rgb);
    }

    public void setHSV(Colour.HSV hsv) {
        this.hsv = hsv;
        this.rgb = Colour.hsv2rgb(hsv);
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public JsonElement toElement() {
        JsonObject o = new JsonObject();
        JsonObject object = new JsonObject();
        object.add("r", new JsonPrimitive(this.rgb.r));
        object.add("g", new JsonPrimitive(this.rgb.g));
        object.add("b", new JsonPrimitive(this.rgb.b));
        object.add("a", new JsonPrimitive(this.alpha));
        // Wow. I can't believe that this formatting is perfect.
        o.add("__value__", object);
        return o;
    }

    @Override
    public void fromElement(JsonElement ele) {
        JsonObject object = ele.getAsJsonObject();
        if (object.has("__value__")) object = object.get("__value__").getAsJsonObject(); // sloppy but works
        setRGB(new Colour.RGB(object.get("r").getAsDouble(), object.get("g").getAsDouble(), object.get("b").getAsDouble()));
        setAlpha(object.get("a").getAsDouble());
    }
}
