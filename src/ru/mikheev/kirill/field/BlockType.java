package ru.mikheev.kirill.field;

import ru.mikheev.kirill.creatures.CommandType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum BlockType {
    CREATURE,
    FOOD,
    WALL;

    private static final List<BlockType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    public static BlockType getRandomBlockType(){
        Random rnd = new Random();
        return VALUES.get(rnd.nextInt(SIZE));
    }
}
