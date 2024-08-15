package com.fmss.userservice.helper;

import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class LockWrapper {
    private final Lock lock = new ReentrantLock();
    private long lastAccessTime;

    public LockWrapper() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
}