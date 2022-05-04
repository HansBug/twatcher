package com.hansbug.twatcher.thread;

import com.hansbug.twatcher.utils.clock.Clock;
import com.hansbug.twatcher.utils.clock.OffsetClock;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ThreadWatcher {
    private enum Status {
        PENDING, WATCHING, SHUTTING_DOWN, ENDED
    }

    private static final int DEFAULT_TIMESTAMP = 100;
    private static final Predicate<Thread> ALL_CHECK = (thread -> true);
    private static final Predicate<Thread> USER_ONLY_CHECK = (thread -> !thread.isDaemon());

    private final int timespan;
    private final Predicate<Thread> watchCheck;
    private final Clock clock;
    private Status status = Status.PENDING;
    private final Object endLockObject = new Object();
    private Thread watchingThread = null;
    private InterruptedException threadError = null;
    private List<ThreadLifetime> result = null;

    public ThreadWatcher(int timespan, Clock clock, Predicate<Thread> watchCheck) {
        this.timespan = timespan;
        this.clock = clock;
        this.watchCheck = watchCheck;
    }

    public ThreadWatcher(int timespan, Clock clock) {
        this(timespan, clock, USER_ONLY_CHECK);
    }

    public ThreadWatcher(int timespan) {
        this(timespan, new OffsetClock());
    }

    public ThreadWatcher() {
        this(DEFAULT_TIMESTAMP);
    }

    public List<ThreadLifetime> getResult() {
        return this.result;
    }

    private void watchLoop() throws InterruptedException {
        this.status = Status.WATCHING;
        Long lastTime = null;

        Thread currentThread = Thread.currentThread();
        HashMap<Long, ThreadLifetimeBuilder> threadMap = new HashMap<>();

        try {
            while (true) {
                synchronized (this.endLockObject) {
                    if (this.status == Status.SHUTTING_DOWN) {
                        break;
                    }
                }

                if (lastTime != null) {
                    long targetTime = lastTime + this.timespan;
                    long timeDelta = targetTime - System.currentTimeMillis();
                    if (timeDelta > 0) {
                        Thread.sleep(timeDelta);
                        lastTime = targetTime;
                    } else {
                        lastTime = System.currentTimeMillis();
                    }
                } else {
                    lastTime = System.currentTimeMillis();
                }

                for (Thread thread : Thread.getAllStackTraces().keySet()) {
                    if (thread != currentThread && this.watchCheck.test(thread)) {
                        if (!threadMap.containsKey(thread.getId())) {
                            threadMap.put(thread.getId(), new ThreadLifetimeBuilder(thread));
                        }

                        ThreadLifetimeBuilder builder = threadMap.get(thread.getId());
                        builder.appendRecord(this.clock.getCurrentTime(), thread.getState());
                    }
                }
            }
        } finally {
            this.result = Collections.unmodifiableList(threadMap.values().stream().map(ThreadLifetime::new).collect(Collectors.toList()));
            this.status = Status.ENDED;
        }
    }

    public synchronized ThreadWatcher start() {
        if (this.status == Status.PENDING) {
            this.watchingThread = new Thread(() -> {
                try {
                    this.watchLoop();
                } catch (InterruptedException e) {
                    this.threadError = e;
                }
            });
            this.watchingThread.setName("ThreadWatcher-" + this.watchingThread.getId());
            this.watchingThread.start();
        }
        return this;
    }

    public synchronized ThreadWatcher shutdown() {
        if (this.status == Status.WATCHING) {
            synchronized (this.endLockObject) {
                this.status = Status.SHUTTING_DOWN;
            }
        }
        return this;
    }

    public ThreadWatcher join() throws InterruptedException {
        if (this.status == Status.WATCHING || this.status == Status.SHUTTING_DOWN) {
            this.watchingThread.join();
            if (this.threadError != null) {
                throw this.threadError;
            }
        }
        return this;
    }
}
