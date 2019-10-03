package ru.mikheev.kirill.creatures;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    private static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    public static Direction getRandomDirection(){
        Random rnd = new Random();
        return VALUES.get(rnd.nextInt(SIZE));

    }
}
