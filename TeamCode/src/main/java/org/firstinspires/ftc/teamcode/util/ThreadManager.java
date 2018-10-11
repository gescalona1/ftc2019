package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.ClassPathOpener;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Ultro
 * ThreadManager.java
 * Purpose: Singleton that manages RobotThread instances
 *
 * @version 1.0 10/11/2018
 */
public class ThreadManager {
    private static volatile ThreadManager threadManager = null;
    private List<Thread> startedThreads = new ArrayList<>();
    private List<RobotThread> threads = new ArrayList<>();

    public void createThread(RobotThread robotThread){
        threads.add(robotThread);
    }

    public void startThread(RobotThread robotThread){
        if(threads.contains(robotThread)){
           Thread t = new Thread(robotThread);
           t.start();
           startedThreads.add(t);

        }
    }

    public void stopThread(RobotThread robotThread){
        if(threads.contains(robotThread)){
            robotThread.stopRunning();
        }
    }

    public void stopAll(){
        for (RobotThread t : threads){
            t.stopRunning();
        }
    }
    /**
     * Private constructor to prevent instantiating this utility class.
     */
    private ThreadManager(){

    }
    /**
     * Get instance of this class (singleton)
     */
    public static ThreadManager getInstance(){
        if(threadManager == null) {
            synchronized (ThreadManager.class){
                if(threadManager == null){
                    threadManager = new ThreadManager();
                }
            }
        }
        return threadManager;
    }
}
