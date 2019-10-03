package ru.mikheev.kirill;

import ru.mikheev.kirill.engine.Engine;

import java.util.concurrent.TimeUnit;

public class SmartAlgorithm {
    public static void main(String[] args){
        Engine engine = new Engine(1, 10, 50, 32, 100);
        engine.start();
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        engine.stopPLS();
    }
}
