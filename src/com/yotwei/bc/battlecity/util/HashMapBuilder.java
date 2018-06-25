package com.yotwei.bc.battlecity.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2018/6/23.
 */
public class HashMapBuilder<K, V> {

    private HashMap<K, V> _map;

    public HashMapBuilder() {
        _map = new HashMap<>();
    }

    public HashMapBuilder<K, V> put(K k, V v) {
        _map.put(k, v);
        return this;
    }

    public HashMap<K, V> build() {
        return _map;
    }
}
