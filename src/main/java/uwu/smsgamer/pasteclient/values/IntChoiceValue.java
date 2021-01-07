package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import uwu.smsgamer.pasteclient.utils.StringHashMap;

public class IntChoiceValue extends ChoiceValue<Integer> {

    public IntChoiceValue(String name, String description, Integer val, StringHashMap<Integer> values, Value<?>... children) {
        super(name, description, val, values, children);
    }

    @Override
    public JsonElement toElement() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromElement(JsonElement ele) {
        value = ele.getAsInt();
    }
}
