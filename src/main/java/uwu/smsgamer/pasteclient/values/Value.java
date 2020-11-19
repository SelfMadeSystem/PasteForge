package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.pasteclient.modules.Module;

import java.util.*;

// Ch
public abstract class Value<T> {
    public Module module;
    protected final String name;
    protected final String description;
    protected T value;
    protected final T dVal;
    protected Value<?> parent;
    protected HashMap<String, Value<?>> children = new HashMap<>();

    protected Value(String name, String description, T dVal) {
        this.name = name;
        this.description = description;
        this.dVal = dVal;
        this.value = dVal;
        // Allow adding of children through FileManager is done per value by overriding the to/fromJson() method.
    }

    public Value(String name, String description, T dVal, Value<?>... children) {
        this(name, description, dVal);
        for (Value<?> child : children) {
            child.parent = this;
            addChild(child);
        }
    }

    public boolean isVisible() {
        return true;
    }

    public boolean isEditable() {
        return true;
    }

    public Value<?> getParent() {
        return parent;
    }

    public HashMap<String, Value<?>> getChildren() {
        return children;
    }

    public Value<?> addChild(Value<?> child) {
        children.put(child.getName().toLowerCase(), child);
        child.module = module;
        return child;
    }

    public void removeChild(Value<?> value) {
        children.entrySet().removeIf(entry -> value.equals(entry.getValue()));
    }

    public Value<?> getChild(@NotNull String arg) {
        return children.get(arg.toLowerCase());
    }

    public T getValue() {
        return value;
    }

    public String getValStr() {
        if (value == null) return "null";
        return value.toString();
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object t) {
        value = (T) t;
    }

    public final void setValueRaw(T t) {
        value = t;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDVal() {
        return dVal;
    }

    public boolean hasChildren() {
        return !this.getChildren().isEmpty() || this.allowChildAdd();
    }

    public boolean allowChildAdd() {
        return false;
    }

    public boolean rightClickRemove() {
        return false;
    }

    public void genNewChild() {
        Throwable throwable = new Throwable("Trying to generate new child in unimplemented object.");
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu =
          Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(throwable);

        Logger logger = LogManager.getLogger();

        // Print our stack trace
        logger.warn(this);
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement traceElement : trace)
            logger.warn("\tat " + traceElement);
    }

    public JsonObject toJSON() {
        JsonObject object = new JsonObject();
        if (children.isEmpty()) {
            object.add(name, toElement());
        } else {
            JsonObject obj = new JsonObject();
            obj.add("__value__", toElement());
            for (Value<?> child : children.values()) {
                obj.add(child.name, child.toJSON().get(child.name));
            }
            object.add(name, obj);
        }
        return object;
    }

    public void fromJSON(JsonObject obj) {
        if (obj.has(name)) {
            if (obj.get(name).isJsonObject()) {
                obj = obj.getAsJsonObject(name);
                fromElement(obj.get("__value__"));
                for (Value<?> child : children.values()) {
                    if (obj.has(child.name)) {
                        JsonObject object = new JsonObject();
                        object.add(child.name, obj.get(child.name));
                        child.fromJSON(object);
                    } else {
                        LogManager.getLogger().warn(child.name + " is not in object. Name: " + name + "  Module: " + module.getName());
                    }
                }
            } else {
                fromElement(obj.get(name));
            }
        } else {
            LogManager.getLogger().warn(name + " is not in object.  Module: " + module.getName());
        }
    }

    public abstract JsonElement toElement();

    public abstract void fromElement(JsonElement ele);
}
