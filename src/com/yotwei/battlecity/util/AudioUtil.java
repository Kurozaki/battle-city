package com.yotwei.battlecity.util;

import com.yotwei.battlecity.game.global.ResourcePool;

import java.applet.AudioClip;

/**
 * Created by YotWei on 2019/4/2.
 */
public class AudioUtil {

    private AudioUtil() {
        throw new UnsupportedOperationException("could not create instance");
    }

    public static void player(String name) {
        AudioClip ac = ResourcePool.getAudioClip(name);
        ac.play();
    }
}
