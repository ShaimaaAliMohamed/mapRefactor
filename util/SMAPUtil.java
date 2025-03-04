package com.sadeem.smap.util;

import com.google.gson.Gson;

public class SMAPUtil {

    public static Gson gson = new Gson();

    public static String partStringValue(String input, String param) {
        return input != null ? input.split(param)[0] : null;
    }
}