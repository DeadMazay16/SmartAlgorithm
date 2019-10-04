package ru.mikheev.kirill.creatures;

import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.interfaces.Drawable;

public class Food implements Drawable {
    private Coordinate coordinate;

    public Food(Coordinate coordinate){
        this.coordinate = coordinate;
    }
    @Override
    public char getConsoleShape() {
        return '*';
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()){
            Food tmp = (Food)obj;
            return tmp.getCoordinate().equals(this.getCoordinate());
        }
        return false;
    }
}
