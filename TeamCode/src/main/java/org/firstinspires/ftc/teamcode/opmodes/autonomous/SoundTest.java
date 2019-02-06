package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.robot.VoiceRecognizer;
import org.firstinspires.ftc.teamcode.util.Position;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "EZ", group = "auto")
public class SoundTest extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 0.75d;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        while(!opModeIsActive()) {
            telemetry.addData("words", VoiceRecognizer.getWords().toString());
            telemetry.update();
        }
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
