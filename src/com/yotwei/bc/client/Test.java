package com.yotwei.bc.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Created by YotWei on 2018/6/12.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        /*
        File file = new File(Const.ROOT_DIR_URL.getFile(), "level/limg2.png");
        BufferedImage img = ImageIO.read(file);

        List<String> lines = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                String temp = "";
                switch (rgb) {
                    case 0xff000000:
                        temp = "01";
                        break;

                    case 0xffffffff:
                        temp = "00";
                        break;

                    case 0xff7f7f7f:
                        temp = "02";
                        break;
                }
                if (x > 0) {
                    line.append(",");
                }
                line.append(temp);
            }
            lines.add(line.toString());
        }

        for (String line : lines) {
            System.out.println("\"" + line + "\",");
        }
        */
    }

    @org.junit.Test
    public void test() {
        String s = "/blueyoshiisjuju/";
        String[] split = s.split("/");
        System.out.println(Arrays.toString(split));
    }

    public void test2(){

    }
}
