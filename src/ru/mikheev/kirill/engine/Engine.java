package ru.mikheev.kirill.engine;

import ru.mikheev.kirill.creatures.CommandType;
import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.creatures.Direction;
import ru.mikheev.kirill.creatures.Food;
import ru.mikheev.kirill.field.BlockType;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import ru.mikheev.kirill.interfaces.Drawable;
import ru.mikheev.kirill.visualization.DrawThread;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

public class Engine extends Thread{

    private final Integer POPULATION_SIZE;
    private final Integer FOOD_COUNT;
    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;
    private Field field;
    private ArrayList<Creature> population;
    private ArrayList<Food> foodPool;
    private DrawThread drawThread;
    private boolean isRunning;
    private long lastUpdate;
    private long timeStep;
    private Random rnd;
    ArrayList<Drawable> toDraw;


    public Engine(Integer populationSize, Integer foodCount, Integer maxFieldX, Integer maxFieldY, Integer memorySize, Integer maxHunger, boolean isByTemplate){
        this.POPULATION_SIZE = populationSize;
        this.MEMORY_SIZE = memorySize;
        this.MAX_HUNGER = maxHunger;
        this.FOOD_COUNT = foodCount;
        rnd = new Random();
        field = new Field(maxFieldX, maxFieldY);
        generateObjects(isByTemplate);
        if(isByTemplate){
            population = new ArrayList<>();
            Creature creature = new Creature(memorySize, maxHunger, new Coordinate(5,5, maxFieldX, maxFieldY));
            creature.createCreatureByTemplate();
            population.add(creature);
        }
        toDraw = new ArrayList<>(population);
        toDraw.addAll(foodPool);
        drawThread = new DrawThread(toDraw, field);
        isRunning = false;
        timeStep = 500;
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
        lastUpdate = System.currentTimeMillis();
        while (isRunning){
            if(lastUpdate + timeStep <= System.currentTimeMillis()){
                lastUpdate = System.currentTimeMillis();
                for (Creature tmp : population){
                    doCommand(tmp);
                }
            }
        }
    }

    private ArrayList<Creature> generateNewPopulation(ArrayList<Coordinate> pool){
        ArrayList<Creature> result = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++){
            Coordinate coordinate = pool.remove(rnd.nextInt(pool.size()));
            Creature tmp = new Creature(MEMORY_SIZE, MAX_HUNGER, coordinate);
            tmp.generateNewCommandList(rnd.nextInt(MEMORY_SIZE));
            result.add(tmp);
        }
        return result;
    }

    private ArrayList<Food> generateNewFoodPool(ArrayList<Coordinate> pool){
        ArrayList<Food> result = new ArrayList<>();
        for (int i = 0; i < FOOD_COUNT; i++){
            Coordinate coordinate = pool.remove(rnd.nextInt(pool.size()));
            Food tmp = new Food(coordinate);
            result.add(tmp);
        }
        return result;
    }

    private void doCommand(Creature creature){
        Creature.Command nextCommand = creature.getNextCommand();
        switch (nextCommand.getCommandType()){
            case EXPLORE:{
                if(nextCommand.getDetectionType() == getCoordinateType(nextCommand.getDirectionCoordinates())){
                    doIfCommand(creature, nextCommand.getIfTrue());
                }else {
                    doIfCommand(creature, nextCommand.getIfFalse());
                }
                break;
            }
            case GRAB:{
                for (Food tmp : foodPool){
                    if(tmp.getCoordinate().equals(nextCommand.getDirectionCoordinates())){
                        foodPool.remove(tmp);
                        toDraw.remove(tmp);
                        creature.feed(100);
                        break;
                    }
                }
                break;
            }
            case MOVE:{
                if(isAbleToMove(nextCommand)) {
                    creature.move(nextCommand.getDirection());
                }
                break;
            }
        }
    }

    private void doIfCommand(Creature creature, Creature.Command command){
        switch (command.getCommandType()){
            case GRAB:{
                for (Food tmp : foodPool){
                    if(tmp.getCoordinate().equals(command.getDirectionCoordinates())){
                        foodPool.remove(tmp);
                        toDraw.remove(tmp);
                        creature.feed(100);
                        break;
                    }
                }
                break;
            }
            case MOVE:{
                if(isAbleToMove(command)) {
                    creature.move(command.getDirection());
                }
                break;
            }
        }
    }

    private void generateObjects(boolean isByTemplate){
        ArrayList<Coordinate> pool = new ArrayList<>();
        for (int i = 0; i < field.getMaxX(); i++){
            for(int j = 0; j < field.getMaxY(); j++){
                pool.add(new Coordinate(i, j,field.getMaxX(),field.getMaxY()));
            }
        }
        if(isByTemplate) {
            population = generateNewPopulation(pool);
        }
        foodPool = generateNewFoodPool(pool);
    }

    private boolean isAbleToMove(Creature.Command command){
        BlockType tmp = getCoordinateType(command.getDirectionCoordinates());
        return tmp == BlockType.EMPTY || tmp == BlockType.FOOD;
    }

    private BlockType getCoordinateType(Coordinate coordinate){
        if(coordinate.isOutSpace()){
            return BlockType.OUT_SPACE;
        }
        for(Creature tmp : population){
            if(tmp.getCoordinate().equals(coordinate)){
                return BlockType.CREATURE;
            }
        }
        for(Food tmp : foodPool){
            if(tmp.getCoordinate().equals(coordinate)){
                return BlockType.FOOD;
            }
        }
        return BlockType.EMPTY;
    }

    public Integer getPopulationSize(){
        return POPULATION_SIZE;
    }
}
