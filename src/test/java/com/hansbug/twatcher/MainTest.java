package com.hansbug.twatcher;


import com.hansbug.twatcher.config.Version;
import org.junit.Test;
import test.hansbug.utils.PrintCapture;

import java.io.IOException;
import java.util.Objects;

public class MainTest {
    @Test
    public void testBasic() {
        assert true;
    }

    @Test
    public void testMainVersion() throws IOException {
        PrintCapture.Captured<Object, ReflectiveOperationException> result = PrintCapture.capture(() ->
                Main.main(new String[]{"-v"}));
        assert result.getReturnValue() == null;
        assert Objects.equals(result.getStderr(), "");

        String stdout = result.getStdout();
        assert stdout.contains("Twatcher");
        assert stdout.contains(String.format("version %s", Version.versionNumber));
    }

    @Test
    public void testMainHelp() throws IOException {
        PrintCapture.Captured<Object, ReflectiveOperationException> result = PrintCapture.capture(() ->
                Main.main(new String[]{"-h"}));
        assert result.getReturnValue() == null;
        assert Objects.equals(result.getStderr(), "");

        String stdout = result.getStdout();
        assert stdout.contains("-h");
        assert stdout.contains("--help");
        assert stdout.contains("-v");
        assert stdout.contains("--version");
        assert stdout.contains("-E");
        assert stdout.contains("--entry");
    }

    @Test
    public void testMainSimple() throws IOException {
        PrintCapture.Captured<Object, ReflectiveOperationException> result = PrintCapture.capture(() ->
                Main.main(new String[]{"-E", "test.hansbug.TestEntry"}));
        assert result.getReturnValue() == null;
        assert Objects.equals(result.getStderr(), "");

        String stdout = result.getStdout();
        assert stdout.contains("test.hansbug.TestEntry is ran.");
        assert stdout.contains("Args is {}.");
    }

    @Test
    public void testMainWithParams() throws IOException {
        PrintCapture.Captured<Object, ReflectiveOperationException> result = PrintCapture.capture(() ->
                Main.main(new String[]{"-E", "test.hansbug.TestEntry", "1", "2", "333"}));
        assert result.getReturnValue() == null;
        assert Objects.equals(result.getStderr(), "");

        String stdout = result.getStdout();
        assert stdout.contains("test.hansbug.TestEntry is ran.");
        assert stdout.contains("Args is {1, 2, 333}.");
    }
}
