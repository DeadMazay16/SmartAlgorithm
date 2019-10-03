package ru.mikheev.kirill.visualization;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class DrawThread extends Thread {

    private ArrayList<Creature> population;
    private HashSet<Coordinate> used;
    private Field field;
    private boolean isRunning;

    public DrawThread(ArrayList<Creature> creatures, HashSet<Coordinate> used, Field field){
        this.population = creatures;
        this.used = used;
        this.field = field;
        isRunning = false;
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
        while (isRunning) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(makeOutput());
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String makeOutput(){
        String output = "";
        for (int i = 0; i < field.getMaxX(); i++) {
            for (int j = 0; j < field.getMaxY(); j++) {
                if (!checkThisCoordinates(i, j)) {
                    output +='.';
                } else {
                    output += '#';
                }
            }
            output += '\n';
        }
        output += population.get(0).getCoordinate().getX() + " " +  population.get(0).getCoordinate().getY() + " " + checkThisCoordinates(population.get(0).getCoordinate().getX(), population.get(0).getCoordinate().getY());
        return  output;
    }

    private boolean checkThisCoordinates(Integer x, Integer y){
        for (Creature tmp : population) {
            if(tmp.getCoordinate().isEqual(x, y)){
                return true;
            }
        }
        return false;
    }
}
