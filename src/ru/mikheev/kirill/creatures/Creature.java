package ru.mikheev.kirill.creatures;

import ru.mikheev.kirill.field.BlockType;
import ru.mikheev.kirill.field.Coordinate;
import ru.mikheev.kirill.interfaces.Drawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Creature implements Drawable {

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
            this(instruction, direction);
            this.detectionType = detectionType;
            this.ifTrue = ift;
            this.ifFalse = iff;
        }

        boolean isExploreType(){
            return instruction == CommandType.EXPLORE;
        }

        public CommandType getCommandType(){
            return instruction;
        }

        public Direction getDirection(){
            return direction;
        }

        public Coordinate getDirectionCoordinates(){
            return coordinate.getDirectionCoordinates(direction);
        }

        public BlockType getDetectionType(){
            return detectionType;
        }

        public Command getIfTrue(){
            return ifTrue;
        }

        public Command getIfFalse(){
            return ifFalse;
        }
    }

    private final Integer MEMORY_SIZE;
    private final Integer MAX_HUNGER;

    private ArrayList<Command> commands;
    private Integer hunger;
    private Integer commandsNumber;
    private Iterator<Command> iterator;
    private Coordinate coordinate;
    private Integer score;

    public Creature(Integer memorySize, Integer maxHunger, Coordinate coordinate){
        this.coordinate = coordinate;
        MAX_HUNGER = maxHunger;
        MEMORY_SIZE = memorySize;
        hunger = MAX_HUNGER / 2;
        commands = new ArrayList<>();
        commandsNumber = 0;
        score = 0;
    }

    private Creature(Creature oldVersion){
        this(oldVersion.getMemorySize(), oldVersion.getMaxHunger(), oldVersion.getCoordinate());
        this.setCommands(oldVersion.getCommands());
        this.setCommandsNumber(oldVersion.getCommandsNumber());
    }

    private void setCommands(ArrayList<Command> newVersion){
        this.commands = (ArrayList<Command>) newVersion.clone();
    }

    private void setCommandsNumber(Integer commandsNumber){
        this.commandsNumber = commandsNumber.intValue();
    }

    private void setCoordinate(Coordinate coordinate){
        this.coordinate = coordinate;
    }

    public Integer getMemorySize() {
        return MEMORY_SIZE;
    }

    public Integer getMaxHunger() {
        return MAX_HUNGER;
    }

    private ArrayList<Command> getCommands(){
        return commands;
    }

    public Command getNextCommand(){
        if(iterator == null){
            iterator = commands.iterator();
        }
        if(!iterator.hasNext()){
            iterator = commands.iterator();
        }
        return iterator.next();
    }

    public Integer getCommandsNumber(){
        return commandsNumber;
    }

    public Integer getSingleCommandsNumber(){
        return commands.size();
    }

    @Override
    public char getConsoleShape() {
        return '@';
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public boolean isAlive(){
        return hunger > 0;
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
        commandsCount++;
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

    private Command generateNewCommand(boolean ableToIf){
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

    public void feed(Integer foodCount){
        hunger += foodCount;
        score += 5;
        if(hunger > MAX_HUNGER){
            hunger = MAX_HUNGER;
        }
    }

    public boolean starvation(){
        hunger -= 1;
        if(isAlive()){
            score += 1;
            return true;
        }
        return false;
    }

    public boolean move(Direction direction){
        return coordinate.move(direction);
    }

    public void setZeroScore(){
        score = 0;
    }

    public void createCreatureByTemplate(){
        ArrayList<Creature.Command> commands = new ArrayList<>();
        commands.add(new Creature.Command(CommandType.EXPLORE, Direction.DOWN, BlockType.FOOD, new Command(CommandType.GRAB, Direction.DOWN), new Command(CommandType.WAIT, Direction.DOWN)));
        commands.add(new Creature.Command(CommandType.EXPLORE, Direction.UP, BlockType.FOOD, new Command(CommandType.GRAB, Direction.UP), new Command(CommandType.WAIT, Direction.UP)));
        commands.add(new Creature.Command(CommandType.EXPLORE, Direction.LEFT, BlockType.FOOD, new Command(CommandType.GRAB, Direction.LEFT), new Command(CommandType.WAIT, Direction.LEFT)));
        commands.add(new Creature.Command(CommandType.EXPLORE, Direction.RIGHT, BlockType.FOOD, new Command(CommandType.GRAB, Direction.RIGHT), new Command(CommandType.WAIT, Direction.RIGHT)));
        commands.add(new Creature.Command(CommandType.MOVE, Direction.RIGHT));
        this.setCommands(commands);
    }

    @Override
    public Creature clone() {
        return new Creature(this);
    }
}
