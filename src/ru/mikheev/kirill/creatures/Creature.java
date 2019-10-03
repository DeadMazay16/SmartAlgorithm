package ru.mikheev.kirill.creatures;

import java.util.ArrayList;

public class Creature {

    static final Integer MEMORY_SIZE = 32;
    static final Integer MAX_HUNGER = 100;

    private class Command{
        private CommandType instruction;
        private Directions direction;
        private Command ifTrue;
        private Command ifFalse;

        Command(CommandType instruction, Directions direction){
            this.instruction = instruction;
            this.direction = direction;
        }

        Command(CommandType instruction, Directions direction, Command ift, Command iff){
            this.instruction = instruction;
            this.direction = direction;
            this.ifTrue = ift;
            this.ifFalse = iff;
        }

        CommandType getCommandCode(){
            return instruction;
        }

        Directions getDirection(){
            return direction;
        }
    }

    private ArrayList<Command> commands;
    private Integer hunger;
    private Integer commandsNumber;

    Creature(){
        hunger = 50;
        commands = new ArrayList<>();
        commandsNumber = 0;
    }

    public void mutate(int genCount){}

    public void generateNewCommandList(int commandsCount){
        while(commandsNumber < commandsCount){
            break;
        }
    }

    public Integer getNextCommand(){return 0;}

}
