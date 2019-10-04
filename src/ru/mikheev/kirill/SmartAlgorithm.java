package ru.mikheev.kirill;

import ru.mikheev.kirill.engine.Engine;

import java.util.concurrent.TimeUnit;

public class SmartAlgorithm {
    public static void main(String[] args){
        Engine engine = new Engine(100, 100, 100, 33, 32, 100, false);
        engine.start();
        try {
            TimeUnit.SECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        engine.stopPLS();
    }
}
