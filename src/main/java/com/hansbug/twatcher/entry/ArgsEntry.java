package com.hansbug.twatcher.entry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.hansbug.twatcher.config.Version;
import com.hansbug.twatcher.thread.ThreadLifetime;
import com.hansbug.twatcher.thread.ThreadWatcher;
import com.hansbug.twatcher.utils.clock.OffsetClock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ArgsEntry {
    @Parameter()
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"-E", "--entry"}, description = "Entry for the expected class.", required = true)
    private String entry;

    @Parameter(names = {"-h", "--help"}, description = "Print help information.", help = true)
    private boolean help;

    @Parameter(names = {"-v", "--version"}, description = "Print version information of twatcher.", help = true)
    private boolean version;

    public List<String> getParameters() {
        return parameters;
    }

    public String getEntry() {
        return entry;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    private String[] getParams() {
        String[] params = new String[parameters.size()];
        parameters.toArray(params);
        return params;
    }

    public void run(JCommander cmd) throws ReflectiveOperationException, InterruptedException {
        if (this.help) {  // print help information
            cmd.usage();
        } else if (this.version) {  // print version information
            System.out.printf("Twatcher, version %s.%n", Version.versionNumber);
            System.out.printf("Release %s is built, at %s.%n", Version.buildShortNumber, Version.buildFullTime);
        } else {
            this.runMain();
        }
    }

    private void runMain() throws ReflectiveOperationException, InterruptedException {
        Class<?> cls = Class.forName(this.entry);
        Method mainMethod = cls.getMethod("main", String[].class);

        OffsetClock clock = new OffsetClock();
        ThreadWatcher watcher = new ThreadWatcher(1, clock);

        try {
            clock.putCurrentTime();
            watcher.start();
            mainMethod.invoke(null, (Object) this.getParams());
        } finally {
            watcher.shutdown().join();
        }

        List<ThreadLifetime> result = watcher.getResult();
        System.err.println(result);
        for (ThreadLifetime life : result) {
            System.err.println(life.getThread().getId());
            System.err.println(life.getThread().getName());
            System.err.println(life.getPoints());
        }
    }
}
