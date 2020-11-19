package uwu.smsgamer.pasteclient.values;

import uwu.smsgamer.pasteclient.utils.StringHashMap;

import java.util.Collection;

public abstract class ChoiceValue<T> extends Value<T> {

    protected StringHashMap<T> choices;

    public ChoiceValue(String name, String description, T val, StringHashMap<T> choices, Value<?>... children) {
        super(name, description, val, children);
        this.choices = choices;
    }

    public StringHashMap<T> getChoices() {
        return choices;
    }

    public String getChoice(T t) {
        return choices.get(t);
    }

    @Override
    public String getValStr() {
        return choices.get(value);
    }

    public Collection<String> getChoicesAsString(T t) {
        return choices.values();
    }
}
