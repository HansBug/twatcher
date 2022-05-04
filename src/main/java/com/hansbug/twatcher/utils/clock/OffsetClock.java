package com.hansbug.twatcher.utils.clock;

public class OffsetClock extends Clock {
    private Long baseTime;

    public OffsetClock(Long baseTime) {
        this.baseTime = baseTime;
    }

    public OffsetClock() {
        this(0L);
    }

    @Override
    public Long getCurrentTime() {
        return System.currentTimeMillis() - baseTime;
    }

    public OffsetClock setBaseTime(Long time) {
        this.baseTime = time;
        return this;
    }

    public OffsetClock putCurrentTime() {
        return this.setBaseTime(System.currentTimeMillis());
    }

    public OffsetClock putZeroTime() {
        return this.setBaseTime(0L);
    }
}
