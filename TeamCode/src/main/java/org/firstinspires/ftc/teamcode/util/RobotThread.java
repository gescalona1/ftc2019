package org.firstinspires.ftc.teamcode.util;

/**
 * Ultro
 * RobotThread.java
 * Purpose: Custom robot threads with more easy access to stop
 *
 * @version 1.0 10/11/2018
 */
public class RobotThread extends Thread {
    public RobotThread(Runnable runnable){
        super(runnable);
    }
}
