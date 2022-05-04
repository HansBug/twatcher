package com.hansbug.twatcher.utils.clock;

import org.junit.Test;

public class ClockTest {
    private static class MyClock extends Clock {
        @Override
        public Long getCurrentTime() {
            long time = System.currentTimeMillis();
            time -= time % 1000;
            return time;
        }
    }

    @Test
    public void testMyClock() {
        MyClock clock = new MyClock();
        assert clock.getCurrentTime() % 1000 == 0;
        assert Math.abs(clock.getCurrentTime() - System.currentTimeMillis()) <= 2000;
    }
}
