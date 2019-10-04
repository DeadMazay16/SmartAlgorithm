package ru.mikheev.kirill;

import ru.mikheev.kirill.engine.Engine;

import java.util.concurrent.TimeUnit;

public class SmartAlgorithm {
    public static void main(String[] args){
        Engine engine = new Engine(10, 100, 50, 10, 32, 100, true);
        engine.start();
        try {
            TimeUnit.SECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        engine.stopPLS();
    }
}
