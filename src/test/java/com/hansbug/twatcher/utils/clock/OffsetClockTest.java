package com.hansbug.twatcher.utils.clock;

import org.junit.Test;

public class OffsetClockTest {
    @Test
    public void testOffsetClock() {
        OffsetClock clock = new OffsetClock();
        assert Math.abs(clock.getCurrentTime() - System.currentTimeMillis()) < 10;
    }

    @Test
    public void testRelativeTime() throws InterruptedException {
        OffsetClock clock = new OffsetClock();
        clock.putCurrentTime();

        Thread.sleep(1000);
        Long time = clock.getCurrentTime();
        assert 990 < time;
        assert time < 1010;

        clock.putZeroTime();
        Thread.sleep(1000);
        assert Math.abs(clock.getCurrentTime() - System.currentTimeMillis()) < 10;
    }
}
