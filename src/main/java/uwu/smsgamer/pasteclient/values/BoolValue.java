package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import uwu.smsgamer.pasteclient.gui.clickgui.utils.Utils;
import uwu.smsgamer.pasteclient.modules.Module;

import java.util.Locale;
import java.util.function.Function;

public class BoolValue extends Value<Boolean> {

    public BoolValue(String name, String description, Boolean val, Value<?>... children) {
        super(name, description, val, children);
    }

    @Override
    public JsonElement toElement() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromElement(JsonElement ele) {
        value = ele.getAsBoolean();
    }
}
