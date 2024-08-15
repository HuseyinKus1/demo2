package com.fmss.api_gateway.config.logger;


import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MyLogger implements Logger {
    private final Level level;
    private final String name;
    private final PrintStream outputStream;
    private final DateTimeFormatter dateFormatter;
    private final MyMDCAdapter myMDCAdapter;
    private final Map<String,Appender> appenders;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BRIGHT_GREEN = "\u001B[92m";

    public MyLogger(Level level, String name, MyMDCAdapter myMDCAdapter, Map<String,Appender> appenders) {
        this.level = level;
        this.name = name;
        this.outputStream = System.out;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault());
        this.myMDCAdapter = myMDCAdapter;
        this.appenders = appenders;

    }

    public void log(Level level, String message, Object... args) {

        if (isLevelEnabled(level)) {
            String formattedMessage = formatMessage(message, args);
            String logEntry = formatLogEntry(level, formattedMessage);
            outputStream.println(logEntry);

            if (args.length > 0 && args[args.length - 1] instanceof Throwable throwable) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                outputStream.println(sw);
            }
        }
    }

    public void sendToRabbit(Level level, String message, Object... args) {
        MyLoggingEvent myLoggingEvent = new MyLoggingEvent(name,level,message,null,args);

        appenders.get("Rabbitmq").append(myLoggingEvent);
    }

    private String formatMessage(String message, Object... args) {
        if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
            Object[] argsWithoutThrowable = new Object[args.length - 1];
            System.arraycopy(args, 0, argsWithoutThrowable, 0, args.length - 1);
            return MessageFormatter.arrayFormat(message, argsWithoutThrowable).getMessage();
        }
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    private String formatLogEntry(Level level, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormatter.format(Instant.now())).append(" ");
        builder.append("[").append(Thread.currentThread().getName()).append("] ");
        builder.append(getColoredLevel(level)).append(" ");
        builder.append(name).append(" - ");

        Map<String, String> mdcMap = myMDCAdapter.getCopyOfContextMap();
        if (mdcMap != null && !mdcMap.isEmpty()) {
            builder.append("{");
            for (Map.Entry<String, String> entry : mdcMap.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
            builder.setLength(builder.length() - 2);
            builder.append("} ");
        }
        builder.append(message).append(ANSI_RESET);
        return builder.toString();
    }

    private String getColoredLevel(Level level) {
        return switch (level) {
            case Level.RABBIT -> null;
            case Level.ERROR -> ANSI_RED + level + ANSI_RESET;
            case Level.WARN -> ANSI_YELLOW + level + ANSI_RESET;
            case Level.INFO -> ANSI_BRIGHT_GREEN+ level + ANSI_RESET;
            case Level.DEBUG -> ANSI_BLUE + level + ANSI_RESET;
            case Level.TRACE -> ANSI_PURPLE + level + ANSI_RESET;
        };
    }

    private boolean isLevelEnabled(Level level) {
        return this.level.toInt() <= level.toInt();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(Level.TRACE);
    }

    @Override
    public void trace(String msg) {
        log(Level.TRACE, msg);
    }

    @Override
    public void trace(String format, Object arg) {
        log(Level.TRACE, format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(Level.TRACE, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return isTraceEnabled();
    }

    @Override
    public void trace(Marker marker, String msg) {
        trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        trace(format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        trace(format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        trace(format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(Level.DEBUG, msg);
    }

    @Override
    public void debug(String format, Object arg) {
        log(Level.DEBUG, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(Level.DEBUG, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        debug(format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        debug(format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        debug(format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg);
    }

    @Override
    public void info(String format, Object arg) {
        log(Level.INFO, format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(Level.INFO, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        log(Level.INFO, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String msg) {
        info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        info(format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        info(format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        info(format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARN);
    }

    @Override
    public void warn(String msg) {
        log(Level.WARN, msg);
    }

    public void rabbit(String msg){
        sendToRabbit(Level.RABBIT, msg);
    }
    public void rabbit(String format, Object arg) {
        sendToRabbit(Level.WARN, format, arg);
    }
    public void rabbit(String format, Object... arguments) {
        sendToRabbit(Level.RABBIT, format, arguments);
    }
    public void rabbit(String format, Object arg1, Object arg2) {
        sendToRabbit(Level.WARN, format, arg1, arg2);
    }

    public void rabbit(String msg, Throwable t) {
        sendToRabbit(Level.WARN, msg, t);
    }
    public void rabbit(Marker marker,String msg){
        sendToRabbit(Level.RABBIT, msg);
    }
    public void rabbit(Marker marker,String format, Object arg) {
        sendToRabbit(Level.WARN, format, arg);
    }
    public void rabbit(Marker marker,String format, Object... arguments) {
        sendToRabbit(Level.RABBIT, format, arguments);
    }
    public void rabbit(Marker marker,String format, Object arg1, Object arg2) {
        sendToRabbit(Level.WARN, format, arg1, arg2);
    }

    public void rabbit(Marker marker,String msg, Throwable t) {
        sendToRabbit(Level.WARN, msg, t);
    }
    @Override
    public void warn(String format, Object arg) {
        log(Level.WARN, format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARN, format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(Level.WARN, format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        warn(format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        warn(format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        warn(format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(Level.ERROR);
    }

    @Override
    public void error(String msg) {
        log(Level.ERROR, msg);
    }

    @Override
    public void error(String format, Object arg) {
        log(Level.ERROR, format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(Level.ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.ERROR, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String msg) {
        error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        error(format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        error(format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        error(format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        error(msg, t);
    }

}