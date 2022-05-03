package com.hansbug.twatcher;

import com.beust.jcommander.JCommander;
import com.hansbug.twatcher.entry.ArgsEntry;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse(args);
        entry.run(cmd);
    }
}