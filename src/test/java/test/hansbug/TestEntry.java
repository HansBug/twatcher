package test.hansbug;

import java.util.Arrays;

public class TestEntry {
    public static void main(String[] args) {
        System.out.println("test.hansbug.TestEntry is ran.");
        System.out.println("Args is {" + String.join(", ", Arrays.asList(args)) + "}.");
    }
}
