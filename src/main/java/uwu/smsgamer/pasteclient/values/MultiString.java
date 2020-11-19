package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class MultiString extends Value<Void> {
    public MultiString(String name, String description) {
        super(name, description, null);
    }

    @Override
    public JsonObject toJSON() {
        JsonObject object = new JsonObject();
        if (children.isEmpty()) {
            object.add(name, toElement());
        } else {
            JsonObject obj = new JsonObject();
            for (Value<?> child : children.values()) {
                obj.add(child.name, child.toJSON().get(child.name));
            }
            object.add(name, obj);
        }
        return object;
    }

    @Override
    public void genNewChild() {
        StringValue val = new StringValue("Entry" + children.size(), "", "") {
            @Override
            public boolean rightClickRemove() {
                return true;
            }
        };
        val.parent = this;
        addChild(val);
    }

    @Override
    public boolean allowChildAdd() {
        return true;
    }

    @Override
    public void fromJSON(JsonObject obj) {
        if (obj.has(name)) {
            if (obj.get(name).isJsonObject()) {
                obj = obj.getAsJsonObject(name);
                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    StringValue val = new StringValue(entry.getKey(), "", entry.getValue().getAsString()) {
                        @Override
                        public boolean rightClickRemove() {
                            return true;
                        }
                    };
                    val.parent = this;
                    addChild(val);
                }
            } else {
                fromElement(obj.get(name));
            }
        } else {
            LogManager.getLogger().warn(name + " is not in object.  Module: " + module.getName());
        }
    }

    @Override
    public JsonElement toElement() {
        return JsonNull.INSTANCE;
    }

    @Override
    public void fromElement(JsonElement ele) {
    }
}
