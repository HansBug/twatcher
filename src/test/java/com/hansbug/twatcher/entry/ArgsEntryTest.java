package com.hansbug.twatcher.entry;

import com.beust.jcommander.JCommander;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

public class ArgsEntryTest {
    @Test
    public void testVersion() {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse("-v");
        assert entry.isVersion();
        assert !entry.isHelp();
        assert entry.getEntry() == null;
        assert entry.getParameters().isEmpty();
    }

    @Test
    public void testHelp() {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse("-h");
        assert !entry.isVersion();
        assert entry.isHelp();
        assert entry.getEntry() == null;
        assert entry.getParameters().isEmpty();
    }

    @Test
    public void testEntryShortVersion() {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse("-E", "helloworld.Main");
        assert !entry.isVersion();
        assert !entry.isHelp();
        assert Objects.equals(entry.getEntry(), "helloworld.Main");
        assert entry.getParameters().isEmpty();
    }

    @Test
    public void testEntryFullVersion() {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse("--entry", "helloworld.Main");
        assert !entry.isVersion();
        assert !entry.isHelp();
        assert Objects.equals(entry.getEntry(), "helloworld.Main");
        assert entry.getParameters().isEmpty();
    }

    @Test
    public void testEntryWithParameters() {
        ArgsEntry entry = new ArgsEntry();
        JCommander cmd = JCommander.newBuilder().addObject(entry).build();
        cmd.parse("-E", "helloworld.Main", "p1", "p2", "p3");
        assert !entry.isVersion();
        assert !entry.isHelp();
        assert Objects.equals(entry.getEntry(), "helloworld.Main");
        assert entry.getParameters().equals(Arrays.asList("p1", "p2", "p3"));
    }
}
