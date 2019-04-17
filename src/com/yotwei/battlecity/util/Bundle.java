package com.yotwei.battlecity.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2019/4/5.
 */
public class Bundle {

    private final Map<String, Object> values;

    public Bundle() {
        this.values = new HashMap<>();
    }

    public <T> boolean set(String key, T value) {
        if (null == value) {
            return values.remove(key) != null;
        } else {
            values.put(key, value);
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) values.get(key);
    }
}
