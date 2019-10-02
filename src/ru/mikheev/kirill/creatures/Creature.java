package ru.mikheev.kirill.creatures;

import java.util.ArrayList;

public class Creature {
    
    static final Integer memorySize = 64;

    private class Command{
        private Integer commandCode;
        private Command ifTrue;
        private Command ifFalse;

        Command(Integer c){
            this.commandCode = c;
        }

        Command(Integer c, Command ift, Command iff){
            this.commandCode = c;
            this.ifTrue = ift;
            this.ifFalse = iff;
        }

        Integer getCommandCode(){
            return commandCode;
        }
    }

    ArrayList<Command> commands;

    Creature(){}

    public void mutate(int genCount){}

    public void generateNewGen(int genCount){}

    public Integer getNextCommand(){return 0;}

}
