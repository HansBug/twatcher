package com.hansbug.twatcher.thread;

import com.hansbug.twatcher.utils.GenericPair;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadLifetime {
    public static class Point {
        private final long time;
        private final Thread.State state;

        private Point(long time, Thread.State state) {
            this.time = time;
            this.state = state;
        }

        public long getTime() {
            return time;
        }

        public Thread.State getState() {
            return state;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "time=" + time +
                    ", state=" + state +
                    '}';
        }
    }

    private final Thread thread;
    private final List<Point> points;

    public ThreadLifetime(Thread thread, List<GenericPair<Long, Thread.State>> points) {
        this.thread = thread;
        this.points = Collections.unmodifiableList(points.stream().map((pair) -> new Point(pair.getFirst(), pair.getSecond())).collect(Collectors.toList()));
    }

    public ThreadLifetime(ThreadLifetimeBuilder builder) {
        this(builder.getThread(), builder.getChangingPoints());
    }

    public Thread getThread() {
        return thread;
    }

    public List<Point> getPoints() {
        return points;
    }
}
