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

    public void reSetX(Integer x){
        this.x = x;
    }

    public void reSetY(Integer y){
        this.y = y;
    }

    public void reSetCoordinate(Integer x, Integer y){
        this.x = x;
        this.y = x;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }

    public boolean move(Direction direction){
        switch (direction){
            case UP:{
                if( y - 1 >= 0){
                    y++;
                    return true;
                }
                return false;
            }
            case RIGHT:{
                if( x + 1 <= maxX){
                    x++;
                    return true;
                }
                return false;
            }
            case LEFT:{
                if( x - 1 >= 0){
                    x++;
                    return true;
                }
                return false;

            }
            case DOWN:{
                if( y + 1 <= maxY){
                    y++;
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
