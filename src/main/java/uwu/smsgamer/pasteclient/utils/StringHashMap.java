package uwu.smsgamer.pasteclient.utils;

import java.util.*;

public class StringHashMap<T> extends HashMap<T, String> {
    public StringHashMap() {
    }

    public StringHashMap(Object... objects) {
        T t = null;
        boolean sw = true;
        for (Object object : objects) {
            if (!sw) {
                put(t, object.toString());
                sw = true;
            } else {
                t = (T) object;
                sw = false;
            }
        }
    }
}
