package com.utils;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JsonParser {
    public static String toJson(Object obj){
        return new Gson().toJson(obj);
    }
    public static <T> T fromJson(String json, Class<T> classOfT){
        return new Gson().fromJson(json, classOfT);
    }
    public static <T> T fromJson(ByteBuffer jsonBB, Class<T> classOfT){
        return fromJson(byteBufferToString(jsonBB, StandardCharsets.UTF_8), classOfT);
    }

    public static String byteBufferToString(ByteBuffer buffer, Charset charset){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }
}
