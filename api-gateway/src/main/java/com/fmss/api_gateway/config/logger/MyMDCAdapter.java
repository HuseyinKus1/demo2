package com.fmss.api_gateway.config.logger;

import org.slf4j.spi.MDCAdapter;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
public class MyMDCAdapter implements MDCAdapter {
    private final ThreadLocal<Map<String, String>> threadLocalMap = new ThreadLocal<>();

    @Override
    public void put(String key, String val) {
        Map<String, String> map = threadLocalMap.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocalMap.set(map);
        }
        map.put(key, val);
    }

    @Override
    public String get(String key) {
        Map<String, String> map = threadLocalMap.get();
        return map != null ? map.get(key) : null;
    }

    @Override
    public void remove(String key) {
        Map<String, String> map = threadLocalMap.get();
        if (map != null) {
            map.remove(key);
        }
    }

    @Override
    public void clear() {
        Map<String, String> map = threadLocalMap.get();
        if (map != null) {
            map.clear();
        }
    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map<String, String> map = threadLocalMap.get();
        return map != null ? new HashMap<>(map) : null;
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        threadLocalMap.set(new HashMap<>(contextMap));
    }

    @Override
    public void pushByKey(String key, String value) {
     //-
    }

    @Override
    public String popByKey(String key) {
        return "";
    }

    @Override
    public Deque<String> getCopyOfDequeByKey(String key) {
        return null;
    }

    @Override
    public void clearDequeByKey(String key) {
    threadLocalMap.get().clear();
    }

}