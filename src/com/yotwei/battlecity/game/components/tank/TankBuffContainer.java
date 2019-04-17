package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by YotWei on 2019/4/10.
 */
public class TankBuffContainer implements GOComponent {

    private final Map<Integer, Buff> buffs;

    public TankBuffContainer() {
        this.buffs = new HashMap<>();
    }

    @Override
    public String name() {
        return "BuffContainer";
    }

    public void updateBuffs() {
        Iterator<Map.Entry<Integer, Buff>> itr = buffs.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, Buff> entry = itr.next();
            int tick = entry.getValue().updateTicker();
            if (tick == 0) {
                itr.remove();
            }
        }
    }

    public void addBuff(Buff buff) {
        buffs.put(buff.getType(), buff);
    }

    @SuppressWarnings("unchecked")
    public <T> Buff<T> getBuff(int type) {
        if (buffs.containsKey(type))
            return (Buff<T>) buffs.get(type);
        return null;
    }
}
