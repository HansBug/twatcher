package com.hansbug.twatcher.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;

public class GenericPairTest {
    @Test
    public void testBasic() {
        GenericPair<Integer, String> p1 = new GenericPair<>(1, "233");
        assert p1.getFirst() == 1;
        assert Objects.equals(p1.getSecond(), "233");
    }

    @Test
    public void testEquals() {
        GenericPair<Integer, String> p1 = new GenericPair<>(1, "233");
        assert p1.equals(p1);
        assert p1.equals(new GenericPair<>(1, "233"));
        assert !p1.equals(null);
        assert !p1.equals(new GenericPair<>(1, 2));
        assert !p1.equals(new GenericPair<>(2, "233"));
        assert !p1.equals(new GenericPair<>(1, "2333"));
        assert !p1.equals(new GenericPair<>(2, "2333"));
    }

    @Test
    public void testHashcode() {
        GenericPair<Integer, String> p1 = new GenericPair<>(1, "233");
        GenericPair<Integer, String> p2 = new GenericPair<>(1, "2333");
        GenericPair<Integer, String> p3 = new GenericPair<>(1, "233");
        HashMap<GenericPair<Integer, String>, Integer> hashMap = new HashMap<>();
        hashMap.put(p1, 1);
        hashMap.put(p2, 3);
        assert hashMap.get(p1) == 1;
        assert hashMap.get(p2) == 3;
        assert hashMap.get(p3) == 1;
    }

    @Test
    public void testToString() {
        GenericPair<Integer, String> p1 = new GenericPair<>(1, "23x3");
        assert Objects.equals(p1.toString(), "GenericPair{first=1, second=23x3}");
    }
}
