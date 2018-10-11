package org.firstinspires.ftc.teamcode.util;

/**
 * Ultro
 * RobotThread.java
 * Purpose: Custom robot threads with more easy access to stop
 *
 * @version 1.0 10/11/2018
 */
public abstract class RobotThread implements Runnable {
    private boolean running = true;
    @Override
    public void run() {
        if (!running) return;
        while(running){
            doRun();
        }
    }

    public void doRun() {};

    public void stopRunning(){
        running = false;
    }

}
