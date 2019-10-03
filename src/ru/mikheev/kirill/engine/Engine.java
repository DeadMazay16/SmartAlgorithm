package ru.mikheev.kirill.engine;

import ru.mikheev.kirill.creatures.Creature;
import ru.mikheev.kirill.field.Field;
import java.util.ArrayList;
import java.util.Random;

public class Engine {

    private final Integer POPULATION_SIZE;
    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;
    private Field field;
    private ArrayList<Creature> population;


    public Engine(Integer populationSize, Integer maxFieldX, Integer maxFieldY, Integer memorySize, Integer maxHunger){
        this.POPULATION_SIZE = populationSize;
        this.MEMORY_SIZE = memorySize;
        this.MAX_HUNGER = maxHunger;
        field = new Field(maxFieldX, maxFieldY);
        population = generateNewPopulation();
    }

    public Integer getPopulationSize(){
        return POPULATION_SIZE;
    }

    private ArrayList<Creature> generateNewPopulation(){
        ArrayList<Creature> result = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++){
            Creature tmp = new Creature(MEMORY_SIZE, MAX_HUNGER);
            tmp.generateNewCommandList(rnd.nextInt(MEMORY_SIZE));
            result.add(tmp);
        }
        return result;
    }

}
