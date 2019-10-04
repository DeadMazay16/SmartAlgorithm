package ru.mikheev.kirill.field;

import ru.mikheev.kirill.creatures.Direction;

public class Coordinate {
    private Integer x;
    private Integer y;
    private final Integer maxX;
    private final Integer maxY;


    public Coordinate(Integer x, Integer y, Integer maxX, Integer maxY){
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void reSetCoordinate(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }

    public boolean isOutSpace(){
        return x < 0 || y < 0 || x > maxX || y > maxY;
    }

    public Coordinate getDirectionCoordinates(Direction direction){
        Integer newX = this.x;
        Integer newY = this.y;
        switch (direction){
            case UP:{
                newY--;
                break;
            }
            case RIGHT:{
                newX++;
                break;
            }
            case LEFT:{
                newX--;
                break;
            }
            case DOWN:{
                newY++;
                break;
            }
        }
        return new Coordinate(newX, newY, this.maxX, this.maxY);
    }

    public boolean move(Direction direction){
        switch (direction){
            case UP:{
                if( y - 1 >= 0){
                    y--;
                    return true;
                }
                return false;
            }
            case RIGHT:{
                if( x + 1 < maxX){
                    x++;
                    return true;
                }
                return false;
            }
            case LEFT:{
                if( x - 1 >= 0){
                    x--;
                    return true;
                }
                return false;

            }
            case DOWN:{
                if( y + 1 < maxY){
                    y++;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean isEqual(Integer x, Integer y){
        return this.x == x && this.y == y;
    }

    @Override
    public int hashCode() {
        return 1000 * x + y + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()) {
            Coordinate coordinate = (Coordinate) obj;
            return (coordinate.getX() == this.x && coordinate.getY() == this.y);
        }else{
            return false;
        }
    }
}
