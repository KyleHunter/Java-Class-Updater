package eUpdater.misc;

/**
 * Created by Kyle on 11/16/2015.
 */
public class timer {
    private long startTime;

    public timer(){
    }

    public timer(boolean startTime){
        if (startTime)
            startTime();
    }

    public void startTime(){
        startTime = System.nanoTime() / 1000000;
    }

    public long getElapsedTime(){
        return (System.nanoTime() / 1000000) - startTime;
    }

}
