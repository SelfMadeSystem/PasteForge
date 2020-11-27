package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;

public class VoidValue extends Value<Void> {
    public VoidValue(String name, String description) {
        super(name, description, null);
    }

    public VoidValue(String name, String description, Value<?>... children) {
        super(name, description, null, children);
    }

    @Override
    public JsonElement toElement() {
        return JsonNull.INSTANCE;
    }

    @Override
    public void fromElement(JsonElement ele) {
    }
}
