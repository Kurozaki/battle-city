package com.yotwei.bc.battlecity.util;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by YotWei on 2018/6/12.
 */
public class Debugger {

    private Debugger() {

    }

    public static class Log {
        public static void info(Object o) {
            System.out.println("[INFO] " + o.toString());
        }

        public static void important(Object o) {
            System.err.println("[IMPORTANT] " + o.toString());
        }
    }

    public static class Draw {

        private static final Queue<String> lines = new LinkedList<>();
        private static final Font drawFont = new Font("Consolas", Font.PLAIN, 12);

        public static void addLine(String line) {
            lines.add(line);
        }

        public static void drawLines(Graphics2D gg) {
            int offsetY = drawFont.getSize();
            gg.setColor(Color.WHITE);
            gg.setFont(drawFont);
            while (!lines.isEmpty()) {
                gg.drawString(lines.poll(), drawFont.getSize() >> 1, offsetY);
                offsetY += drawFont.getSize();
            }
        }
    }
}
