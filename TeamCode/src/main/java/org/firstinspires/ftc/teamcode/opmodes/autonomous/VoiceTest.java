package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.speech.SpeechRecognizer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.robot.S;
import org.firstinspires.ftc.teamcode.robot.VoiceRecognizer;
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
@Autonomous(name = "RunTime Test", group = "auto")
public class VoiceTest extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 0.75d;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {

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
