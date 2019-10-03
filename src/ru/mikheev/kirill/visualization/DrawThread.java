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
            for (int i = 0; i < field.getMaxX(); i++) {
                for (int j = 0; j < field.getMaxY(); j++) {
                    if (!used.contains(new Coordinate(i, j, 0, 0))) {
                        System.out.print('.');
                    } else {
                        System.out.print('#');
                    }
                }
                System.out.println();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
