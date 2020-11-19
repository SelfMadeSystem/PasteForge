package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;

public class PositionValue extends Value<Void> {
    public PositionValue(String name, String description) {
        super(name, description, null);
        addChild(new BoolValue("Relative", "Whether or not the position is relative to the player.", false));
        // TODO: 2020-11-18 Figure out a way to have really huge values and really tiny values as well.
    }

    @Override
    public JsonElement toElement() {
        return JsonNull.INSTANCE;
    }

    @Override
    public void fromElement(JsonElement ele) {
    }
}
