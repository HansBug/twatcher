package com.hansbug.twatcher.thread;

import com.hansbug.twatcher.utils.GenericPair;

import java.util.ArrayList;

public class ThreadLifetimeBuilder {
    private final Thread thread;
    private final ArrayList<GenericPair<Long, Thread.State>> fullRecord;

    public ThreadLifetimeBuilder(Thread thread) {
        this.thread = thread;
        this.fullRecord = new ArrayList<>();
    }

    public Thread getThread() {
        return thread;
    }

    public void appendRecord(long time, Thread.State state) {
        this.fullRecord.add(new GenericPair<>(time, state));
    }

    public ArrayList<GenericPair<Long, Thread.State>> getChangingPoints() {
        ArrayList<GenericPair<Long, Thread.State>> points = new ArrayList<>();
        int length = this.fullRecord.size();

        for (int i = 0; i < length; i++) {
            GenericPair<Long, Thread.State> pair = this.fullRecord.get(i);
            if (i == 0 || i == (length - 1) ||
                    pair.getSecond() != this.fullRecord.get(i - 1).getSecond() ||
                    pair.getSecond() != this.fullRecord.get(i + 1).getSecond()) {
                points.add(pair);
            }
        }
        return points;
    }
}
