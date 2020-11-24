package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;

import java.awt.*;

public class ColorValue extends Value<Color> {
    public ColorValue(String name, String description, Color dVal) {
        super(name, description, dVal);
    }

    public ColorValue(String name, String description, Color dVal, Value<?>... children) {
        super(name, description, dVal, children);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            super.setValueRaw(new Color(((Number) value).intValue()));
        } else if (value instanceof Color) {
            super.setValueRaw((Color) value);
        }
    }

    @Override
    public JsonElement toElement() {
        return new JsonPrimitive(value.getRGB());
    }

    @Override
    public void fromElement(JsonElement ele) {
        setValue(ele.getAsInt());
    }
}
