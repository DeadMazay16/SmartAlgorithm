package ru.mikheev.kirill.creatures;

import ru.mikheev.kirill.field.BlockType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Creature {

    public class Command{
        private CommandType instruction;
        private Direction direction;
        private BlockType detectionType;
        private Command ifTrue;
        private Command ifFalse;

        Command(CommandType instruction, Direction direction){
            this.instruction = instruction;
            this.direction = direction;
        }

        Command(CommandType instruction, Direction direction, BlockType detectionType, Command ift, Command iff){
            this.instruction = instruction;
            this.direction = direction;
            this.detectionType = detectionType;
            this.ifTrue = ift;
            this.ifFalse = iff;
        }

        boolean isExploreType(){
            return instruction == CommandType.EXPLORE;
        }

        CommandType getCommandType(){
            return instruction;
        }

        Direction getDirection(){
            return direction;
        }

        BlockType getDetectionType(){
            return detectionType;
        }

        Command getIfTrue(){
            return ifTrue;
        }

        Command getIfFalse(){
            return ifFalse;
        }
    }

    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;

    private ArrayList<Command> commands;
    private Integer hunger;
    private Integer commandsNumber;

    public Creature(Integer memorySize, Integer maxHunger){
        MAX_HUNGER = maxHunger;
        MEMORY_SIZE = memorySize;
        hunger = MAX_HUNGER / 2;
        commands = new ArrayList<>();
        commandsNumber = 0;
    }

    private Creature(Creature oldVersion){
        this(oldVersion.getMemorySize(), oldVersion.getMaxHunger());
        this.setCommands(oldVersion.getCommands());
        this.setCommandsNumber(oldVersion.getCommandsNumber());
    }

    public boolean mutate(int genCount){
        if(genCount > commands.size()){
            return false;
        }
        Random rnd = new Random();
        HashSet<Integer> alreadyRework = new HashSet<>();
        for (int i = 0; i < genCount; i++){
            Integer tmp = rnd.nextInt(commands.size());
            while (alreadyRework.contains(tmp)){
                tmp = rnd.nextInt(commands.size());
            }
            alreadyRework.add(tmp);
            Command newGen = generateNewCommand(((commandsNumber + 3 <= MEMORY_SIZE) || (commands.get(tmp).isExploreType())));
            if(newGen.isExploreType() && !commands.get(tmp).isExploreType()){
                commandsNumber += 2;
            }
            if(!newGen.isExploreType() && commands.get(tmp).isExploreType()){
                commandsNumber -= 2;
            }
            commands.set(tmp, newGen);
        }
        return true;
    }

    public void generateNewCommandList(int commandsCount){
        while(commandsNumber < commandsCount){
            Command tmp = generateNewCommand(commandsCount - commandsNumber >= 3);
            if(tmp.isExploreType()) {
                commandsNumber += 3;
            }else{
                commandsNumber++;
            }
            commands.add(tmp);
        }
    }

    Command generateNewCommand(boolean ableToIf){
        CommandType ct = CommandType.getRandomCommandType(ableToIf);
        Command tmp;
        if(ct.isExplore()) {
            tmp = new Command(ct, Direction.getRandomDirection(), BlockType.getRandomBlockType(),
                    new Command(CommandType.getRandomCommandType(false), Direction.getRandomDirection()),
                    new Command(CommandType.getRandomCommandType(false), Direction.getRandomDirection()));
        }else{
            tmp = new Command(ct, Direction.getRandomDirection());
        }
        return tmp;
    }

    public Command getNextCommand(){return null;}
    public Integer getCommandsNumber(){return commandsNumber;}
    public Integer getSingleCommandsNumber(){return commands.size();}
    public void feed(Integer foodCount){
        hunger += foodCount;
        if(hunger > MAX_HUNGER){
            hunger = MAX_HUNGER;
        }
    }
    public boolean starvation(){
        hunger -= 1;
        if(isAlive()){
            return true;
        }
        return false;
    }
    public boolean isAlive(){
        return hunger > 0;
    }
    private ArrayList<Command> getCommands(){
        return commands;
    }
    private void setCommands(ArrayList<Command> newVersion){
        this.commands = (ArrayList<Command>) newVersion.clone();
    }
    private void setCommandsNumber(Integer commandsNumber){
        this.commandsNumber = commandsNumber.intValue();
    }
    public Integer getMemorySize() {
        return MEMORY_SIZE;
    }
    public Integer getMaxHunger() {
        return MAX_HUNGER;
    }

    @Override
    public Creature clone() {
        return new Creature(this);
    }
}
