package com.bondarenko.movieland.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLoggerUtil {
    public static long start(String taskName) {
        long start = System.currentTimeMillis();
        log.info(">>> [{}] started in thread {} at {}",
                taskName, System.identityHashCode(Thread.currentThread()), start);
        return start;
    }

    public static void end(String taskName, long start) {
        long end = System.currentTimeMillis();
        log.info("<<< [{}] finished in thread {} at {}. Duration: {} ms ({} s)",
                taskName, System.identityHashCode(Thread.currentThread()), end,
                (end - start), (end - start) / 1000.0);
    }
}
