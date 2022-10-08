package com.xiaoxiong.compont;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiongliang
 * @version 1.0
 * @since 2022/1/28 18:28
 */
@Slf4j
public class NamedThreadFactory_Test {

    @Test
    public void test_atomicInteger() {
        log.info("test");
        System.out.println(new AtomicInteger(1).getAndIncrement());
    }

    @Test
    public void test_threadFactory() {
        System.out.println(new NamedThreadFactory("test-thread-factory").newThread(() -> System.out.println("test")).getName());
    }

}
