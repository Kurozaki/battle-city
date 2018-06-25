package com.yotwei.bc.battlecity.util;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YotWei on 2018/6/13.
 */
public class FileUtil {

    private FileUtil() {

    }

    public static List<String> readTextFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String temp;
        while ((temp = br.readLine()) != null)
            lines.add(temp);
        return lines;
    }

    public static JSONObject readJsonFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null)
            sb.append(temp);
        return new JSONObject(sb.toString());
    }

}
