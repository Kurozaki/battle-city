package com.yotwei.battlecity._test;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.util.Constant;

import java.io.File;
import java.io.IOException;

/**
 * Created by YotWei on 2019/2/26.
 */
public class Test {

    public static void main(String[] args) throws IOException {

        String str = "wYDN4UXJ4cTN2UXJHÖ0Q5UXJ1cjQ4UXJ";
        for (int i = str.length() - 1; i >= 0; i--) {
            System.out.print(str.charAt(i));
        }
    }
}
