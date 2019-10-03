package ru.mikheev.kirill.engine;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import ru.mikheev.kirill.visualization.DrawThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine {

    private final Integer POPULATION_SIZE;
    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;
    private Field field;
    private ArrayList<Creature> population;
    private HashSet<Coordinate> alreadyUsedCoordinates;
    private DrawThread drawThread;


    public Engine(Integer populationSize, Integer maxFieldX, Integer maxFieldY, Integer memorySize, Integer maxHunger){
        this.POPULATION_SIZE = populationSize;
        this.MEMORY_SIZE = memorySize;
        this.MAX_HUNGER = maxHunger;
        field = new Field(maxFieldX, maxFieldY);
        alreadyUsedCoordinates = new HashSet<>();
        population = generateNewPopulation();
        drawThread = new DrawThread(population, alreadyUsedCoordinates, field);
        drawThread.start();
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drawThread.stopPLS();
    }

    public Integer getPopulationSize(){
        return POPULATION_SIZE;
    }

    private ArrayList<Creature> generateNewPopulation(){
        ArrayList<Creature> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++){
            Coordinate coordinate = new Coordinate(rnd.nextInt(field.getMaxX()), rnd.nextInt(field.getMaxY()), field.getMaxX(), field.getMaxY());
            while (alreadyUsedCoordinates.contains(coordinate)){
                coordinate.reSetCoordinate(rnd.nextInt(field.getMaxX()), rnd.nextInt(field.getMaxY()));
            }
            Creature tmp = new Creature(MEMORY_SIZE, MAX_HUNGER, coordinate);
            alreadyUsedCoordinates.add(coordinate);
            tmp.generateNewCommandList(rnd.nextInt(MEMORY_SIZE));
            result.add(tmp);
        }
        return result;
    }
}
