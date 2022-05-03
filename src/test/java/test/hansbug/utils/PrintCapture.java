package test.hansbug.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class PrintCapture {
    public static class Captured<T, E extends Throwable> {
        private final String stdout;
        private final String stderr;
        private final T returnValue;
        private final E throwable;

        public Captured(String stdout, String stderr, T returnValue, E throwable) {
            this.stdout = stdout;
            this.stderr = stderr;
            this.returnValue = returnValue;
            this.throwable = throwable;
        }

        public String getStdout() {
            return stdout;
        }

        public String getStderr() {
            return stderr;
        }

        public T getReturnValue() {
            return returnValue;
        }

        public E getThrowable() {
            return throwable;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public interface Capturable<T, E extends Throwable> {
        T run() throws E;
    }

    public interface VoidCapturable<E extends Throwable> {
        void run() throws E;
    }

    public static synchronized <T, E extends Throwable> Captured<T, E> capture(Capturable<T, E> target)
            throws IOException {
        T result = null;
        E error = null;
        String stdoutResult;
        String stderrResult;
        PrintStream stdout = System.out;
        PrintStream stderr = System.err;

        final String utf8 = StandardCharsets.UTF_8.name();
        try (
                ByteArrayOutputStream sout = new ByteArrayOutputStream();
                ByteArrayOutputStream serr = new ByteArrayOutputStream()
        ) {
            try (
                    PrintStream newStdout = new PrintStream(sout, true, utf8);
                    PrintStream newStderr = new PrintStream(serr, true, utf8)
            ) {
                System.setOut(newStdout);
                System.setErr(newStderr);
                result = target.run();
            } catch (Throwable err) {
                //noinspection unchecked
                error = (E) err;
            } finally {
                System.setOut(stdout);
                System.setErr(stderr);
            }
            stdoutResult = sout.toString();
            stderrResult = serr.toString();
        }

        return new Captured<>(stdoutResult, stderrResult, result, error);
    }

    public static synchronized <T, E extends Throwable> Captured<T, E> capture(VoidCapturable<E> target)
            throws IOException {
        return PrintCapture.capture(() -> {
            target.run();
            return null;
        });
    }
}
