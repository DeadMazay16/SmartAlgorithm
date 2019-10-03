package ru.mikheev.kirill.engine;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.field.BlockType;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import ru.mikheev.kirill.visualization.DrawThread;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Engine extends Thread{

    private final Integer POPULATION_SIZE;
    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;
    private Field field;
    private ArrayList<Creature> population;
    private HashSet<Coordinate> alreadyUsedCoordinates;
    private DrawThread drawThread;
    private boolean isRunning;


    public Engine(Integer populationSize, Integer maxFieldX, Integer maxFieldY, Integer memorySize, Integer maxHunger){
        this.POPULATION_SIZE = populationSize;
        this.MEMORY_SIZE = memorySize;
        this.MAX_HUNGER = maxHunger;
        field = new Field(maxFieldX, maxFieldY);
        alreadyUsedCoordinates = new HashSet<>();
        population = generateNewPopulation();
        drawThread = new DrawThread(population, alreadyUsedCoordinates, field);
        Random rnd = new Random();
        isRunning = false;
    }

    @Override
    public synchronized void start() {
        drawThread.start();
        isRunning = true;
        super.start();
    }

    public void stopPLS(){
        isRunning = false;
        drawThread.stopPLS();
    }

    @Override
    public void run() {
        while (isRunning){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Creature tmp : population){
                doCommand(tmp);
            }
        }
    }

    public Integer getPopulationSize(){
        return POPULATION_SIZE;
    }

    private ArrayList<Creature> generateNewPopulation(){
        ArrayList<Creature> result = new ArrayList<>();
        Random rnd = new Random();
        ArrayList<Coordinate> pool = new ArrayList<>();
        for (int i = 0; i < field.getMaxX(); i++){
            for(int j = 0; j < field.getMaxY(); j++){
                pool.add(new Coordinate(i, j,field.getMaxX(),field.getMaxY()));
            }
        }
        for (int i = 0; i < POPULATION_SIZE; i++){
            Coordinate coordinate = pool.remove(rnd.nextInt(pool.size()));
            Creature tmp = new Creature(MEMORY_SIZE, MAX_HUNGER, coordinate);
            alreadyUsedCoordinates.add(coordinate);
            tmp.generateNewCommandList(rnd.nextInt(MEMORY_SIZE));
            result.add(tmp);
        }
        return result;
    }

    private BlockType getCoordinateType(Coordinate coordinate){
        if(coordinate.isOutSpace()){
            return BlockType.OUT_SPACE;
        }
        if(alreadyUsedCoordinates.contains(coordinate)){
            return BlockType.CREATURE;
        }
        return BlockType.EMPTY;
    }

    private void doCommand(Creature creature){
        Creature.Command nextCommand = creature.getNextCommand();
        switch (nextCommand.getCommandType()){
            case EXPLORE:{
                if(nextCommand.getDetectionType() == getCoordinateType(nextCommand.getDetectionCoordinates())){
                    doIfCommand(creature, nextCommand.getIfTrue());
                }else {
                    doIfCommand(creature, nextCommand.getIfTrue());
                }
                break;
            }
            case GRAB:{
                break;
            }
            case MOVE:{
                creature.move(nextCommand.getDirection());
                break;
            }
        }
    }

    private void doIfCommand(Creature creature, Creature.Command command){
        switch (command.getCommandType()){
            case GRAB:{
                break;
            }
            case MOVE:{
                creature.move(command.getDirection());
                break;
            }
        }
    }
}
