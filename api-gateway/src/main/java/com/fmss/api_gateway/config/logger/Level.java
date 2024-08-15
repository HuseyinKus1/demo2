package com.fmss.api_gateway.config.logger;

public enum Level{
    RABBIT(50, "RABBIT"),
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, "TRACE");

    private final int levelInt;
    private final String levelStr;

    Level(int i, String s) {
        this.levelInt = i;
        this.levelStr = s;
    }

    public int toInt() {
        return this.levelInt;
    }

    @Override
    public String toString() {
        return this.levelStr;
    }
}
