package com.xiaoxiong.compont;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/28 18:24
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String name;

    private static final AtomicInteger counter = new AtomicInteger(1);

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + "-" + counter.getAndIncrement());
    }
}
