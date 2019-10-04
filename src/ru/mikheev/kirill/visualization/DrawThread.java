package ru.mikheev.kirill.visualization;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.engine.Engine;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import ru.mikheev.kirill.interfaces.Drawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DrawThread extends Thread {

    private ArrayList<Drawable> objects;
    private Field field;
    private boolean isRunning;
    private long lastUpdate;
    private long timeStep;
    private Engine parent;

    public DrawThread(ArrayList<Drawable> objects, Field field, Engine parent){
        this.parent = parent;
        this.objects = objects;
        this.field = field;
        isRunning = false;
        timeStep = 100;
    }

    public void stopPLS(){
        isRunning = false;
    }
    @Override
    public synchronized void start() {
        isRunning = true;
        super.start();
    }

    @Override
    public void run() {
        lastUpdate = System.currentTimeMillis();
        while (isRunning) {
            if(lastUpdate + timeStep <= System.currentTimeMillis()){
                lastUpdate = System.currentTimeMillis();
                String output = makeOutput();
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.print(output);
            }
        }
    }

    private String makeOutput(){
        String output = "";
        parent.deleteMissingObjects();
        for (int i = -1; i < field.getMaxY() + 1; i++) {
            for (int j = -1; j < field.getMaxX() + 1; j++) {
                if (i < 0 || j < 0 || i >= field.getMaxY() || j >= field.getMaxX()) {
                    output += (i < 0 ||  i >= field.getMaxY() ? '=' : '|');
                } else{
                    output += checkThisCoordinates(j, i);
                }
            }
            output += '\n';
        }
        output += parent.getPopulationSize() + " creatures left";
        return  output;
    }

    private char checkThisCoordinates(Integer x, Integer y){
        for (Drawable tmp : objects) {
            if(tmp.getCoordinate().isEqual(x, y)){
                return tmp.getConsoleShape();
            }
        }
        return ' ';
    }
}
