package com.hansbug.twatcher;

import com.beust.jcommander.JCommander;
import com.hansbug.twatcher.entry.ArgsEntry;

public abstract class Main {
    public static void main(String[] args) throws ReflectiveOperationException, InterruptedException {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse(args);
        entry.run(cmd);
    }
}