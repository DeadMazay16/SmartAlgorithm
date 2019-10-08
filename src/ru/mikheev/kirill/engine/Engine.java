package ru.mikheev.kirill.engine;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.creatures.Food;
import ru.mikheev.kirill.field.BlockType;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.field.Field;
import ru.mikheev.kirill.interfaces.Drawable;
import ru.mikheev.kirill.visualization.DrawThread;

import java.util.*;

public class Engine extends Thread{

    private final Integer POPULATION_SIZE;
    private final Integer FOOD_COUNT;
    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;
    private ArrayList<Creature> population;
    private ArrayList<Creature> generationResult;
    private ArrayList<Drawable> toDraw;
    private ArrayList<Drawable> missingObjects;
    private ArrayList<Food> foodPool;
    private DrawThread drawThread;
    private boolean drawThreadReady;
    private boolean isRunning;
    private boolean templated;
    private boolean deleted;
    private long timeStep;
    private Field field;
    private Random rnd;
    private Integer generationNumber;

    public Engine(Integer populationSize, Integer foodCount, Integer maxFieldX, Integer maxFieldY, Integer memorySize, Integer maxHunger, boolean templated){
        this.POPULATION_SIZE = populationSize;
        this.MEMORY_SIZE = memorySize;
        this.MAX_HUNGER = maxHunger;
        this.FOOD_COUNT = foodCount;
        this.templated = templated;
        generationNumber = 0;
        drawThreadReady = false;
        deleted = false;
        rnd = new Random();
        field = new Field(maxFieldX, maxFieldY);
        isRunning = false;
        timeStep = 500;
        generateFirstGeneration();
    }

    private void newGenerationFirstSets(){
        generationNumber++;
        missingObjects = new ArrayList<>();
        generationResult = new ArrayList<>();
        toDraw = new ArrayList<>();
        toDraw.addAll(population);
        toDraw.addAll(foodPool);
        drawThread = new DrawThread(field, this);
        drawThread.setObjects(toDraw);
        drawThread.start();
    }

    @Override
    public synchronized void start() {
        newGenerationFirstSets();
        isRunning = true;
        super.start();
    }

    @Override
    public void run() {
        long lastUpdate = System.currentTimeMillis();
        while (isRunning){
            if(lastUpdate + timeStep <= System.currentTimeMillis()){
                lastUpdate = System.currentTimeMillis();
                for (Creature tmp : population){
                    doCommand(tmp);
                    boolean isAlive = tmp.starvation();
                    if(!isAlive){
                        missingObjects.add(tmp);
                        generationResult.add(tmp);
                    }
                }
                for(Drawable tmp : missingObjects){
                    if(tmp instanceof Creature){
                        population.remove(tmp);
                    }
                }
                if(!(population.size() > 0)){
                    drawThread.stopPLS();
                    try {
                        drawThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    generateNewGeneration();
                    newGenerationFirstSets();
                }
            }
        }
    }

    public void stopPLS(){
        isRunning = false;
        drawThread.stopPLS();
    }

    private void generateFirstGeneration(){
        generateObjects();
        if(templated){
            population = new ArrayList<>();
            Creature creature = new Creature(MEMORY_SIZE, MAX_HUNGER, new Coordinate(5,5, field.getMaxX(), field.getMaxY()));
            creature.createCreatureByTemplate();
            population.add(creature);
        }
        toDraw = new ArrayList<>(population);
        toDraw.addAll(foodPool);
    }

    private void generateNewGeneration(){
        Collections.sort(generationResult, (Creature first, Creature second) ->
                Integer.compare(first.getScore(), second.getScore())
            );
        Collections.reverse(generationResult);
        ArrayList<Creature> newPopulation = new ArrayList<>();
        ArrayList<Creature> badCreatures = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE / 2; i++){
            newPopulation.add(generationResult.get(i));
            badCreatures.add(generationResult.get(i + POPULATION_SIZE / 2));
        }
        ArrayList<Integer> numbersPool = new ArrayList<>();
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE / 2; i++){
            numbersPool.add(i);
        }
        for (int i = 0; i <  (POPULATION_SIZE / 2) / 5; i++){
            Integer randomInt = rnd.nextInt(numbersPool.size());
            toRemove.add(numbersPool.get(randomInt));
            numbersPool.remove(randomInt.intValue());
        }
        for (int i = POPULATION_SIZE / 2 - 1; i >= 0; i--){
            if(toRemove.contains(i)) {
                newPopulation.remove(i);
                newPopulation.add(i, badCreatures.get(i));
            }
        }
        population.addAll(newPopulation);
        for (Creature tmp : newPopulation){
            tmp.setToStartState();
            population.add(new Creature(tmp));
        }
        ArrayList<Coordinate> pool = new ArrayList<>();
        for (int i = 0; i < field.getMaxX(); i++){
            for(int j = 0; j < field.getMaxY(); j++){
                pool.add(new Coordinate(i, j,field.getMaxX(),field.getMaxY()));
            }
        }
        for(Creature tmp : population){
            Integer randomInt = rnd.nextInt(pool.size());
            tmp.setCoordinate(pool.get(randomInt));
            pool.remove(randomInt.intValue());
        }
        foodPool = generateNewFoodPool(pool);
        numbersPool.clear();
        for(int i = 0; i < POPULATION_SIZE; i++){
            numbersPool.add(i);
        }
        for (int i = 0; i < POPULATION_SIZE * 0.1; i++){
            Integer randomInt = rnd.nextInt(numbersPool.size());
            Creature tmp = population.get(numbersPool.get(randomInt));
            tmp.mutate(1);
        }


    }

    private void generateObjects(){
        ArrayList<Coordinate> pool = new ArrayList<>();
        for (int i = 0; i < field.getMaxX(); i++){
            for(int j = 0; j < field.getMaxY(); j++){
                pool.add(new Coordinate(i, j,field.getMaxX(),field.getMaxY()));
            }
        }
        if(!templated) {
            population = generateNewPopulation(pool);
        }
        foodPool = generateNewFoodPool(pool);
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
                        missingObjects.add(tmp);
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
                        missingObjects.add(tmp);
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

    public void deleteMissingObjects(){
        for (Drawable tmp: missingObjects) {
            toDraw.remove(tmp);
        }
        missingObjects.clear();
    }

    public Integer getPopulationSize(){
        return population.size();
    }

    public Integer getFoodNumber(){
        return foodPool.size();
    }

    public Integer getGenerationNumber(){
        return generationNumber;
    }
}
