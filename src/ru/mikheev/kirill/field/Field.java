package ru.mikheev.kirill.field;

public class Field {

    private final Integer maxX;
    private final Integer maxY;

    public Field(Integer maxX, Integer maxY){
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Integer getMaxX() {
        return maxX;
    }

    public Integer getMaxY() {
        return maxY;
    }
}
