package com.yotwei.battlecity.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Objects;

/**
 * Created by YotWei on 2019/2/27.
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * decode a json object from a text file
     *
     * @param file the target file to be decoded
     * @return a json object
     * @throws IOException   file not found
     * @throws JSONException illegal json format
     */
    public static JSONObject createJsonObjectFromFile(File file) throws IOException, JSONException {

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder strJson = new StringBuilder();
        String temp;

        while (!Objects.isNull(temp = br.readLine())) {
            strJson.append(temp);
        }

        return new JSONObject(strJson.toString());
    }
}
