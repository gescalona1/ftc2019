package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.ClassPathOpener;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;

import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Runnable, Thread> runnableThreadsMap = new HashMap<>();
    private List<Thread> threads = new ArrayList<>();

    public void createThread(Runnable runnable){
        if(runnableThreadsMap.containsKey(runnable)) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            runnableThreadsMap.put(runnable, thread);
        }
    }

    public void addThread(Thread thread){
        threads.add(thread);
    }

    public void deleteThread(Thread robotThread){
        threads.remove(robotThread);
        runnableThreadsMap.remove(robotThread);
        robotThread.interrupt();
    }

    public void startThread(Thread thread){
        if(threads.contains(thread)){
            thread.start();
        }
    }
    public void startThread(Runnable runnable){
        if(threads.contains(runnableThreadsMap.get(runnable))){
            startThread(runnableThreadsMap.get(runnable));
        }
    }

    public void stopThread(Thread robotThread){
        if(threads.contains(robotThread)){
            robotThread.interrupt();
            deleteThread(robotThread);
        }
    }
    public void stopThread(Runnable runnable){
        if(threads.contains(runnableThreadsMap.get(runnable))){
            Thread t = runnableThreadsMap.get(runnable);
            stopThread(t);
        }
    }

    public void stopAll(){
        for (Thread t : threads){
            stopThread(t);
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
