package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.speech.SpeechRecognizer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.robot.S;
import org.firstinspires.ftc.teamcode.robot.VoiceRecognizer;
import org.firstinspires.ftc.teamcode.util.FindMineralRunnable;
import org.firstinspires.ftc.teamcode.util.Position;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "Land and Sample", group = "auto")
public class LandAndSample extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 0.75d;
    private final int CHECKS = 5;
    private AutoHelper autoHelper = new AutoHelper(this);
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        FindMineralRunnable.setRecordData(true);
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        int i = 0;
        position = FindMineralRunnable.getCurrentPosition();
        while(true){
            if(isStopRequested()) break;
            if(i >= CHECKS) {
                FindMineralRunnable.forceStop();
                telemetry.addLine(String.format("Passed accuracy check: Position %s", position));
                break;
            }
            if(position == FindMineralRunnable.getCurrentPosition()) i++;
            else {
                run();
                return; // make sure it only runs this once
            }
            if(getRuntime() > 5){ //if 5 seconds elapsed without it being accurate, just set it
                position = FindMineralRunnable.getCurrentPosition();
                telemetry.addLine("Runtime too long, going over");
                break;
            }
        }
        autoHelper.land();
        autoHelper.knock(position);
        /*
        if (opModeIsActive()) {

            driver.mechanumDriveForward(1);
            sleep(5 * 1000);
            driver.mechanumDriveForward(0);
            if (getTfod() != null) {
                getTfod().shutdown();
            }
        }
        */
    }
}
