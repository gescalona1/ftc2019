package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.ThreadManager;

/**
 * Ultro
 * Main.java
 * Purpose: Get common singleton methods (probably useless)
 *
 * @version 1.0 10/11/2018
 */
public class Main {
    public static RobotDriver getDriver(){
        return RobotDriver.getDriver();
    }

    public static ThreadManager getThreadManager(){
        return ThreadManager.getInstance();
    }
}
