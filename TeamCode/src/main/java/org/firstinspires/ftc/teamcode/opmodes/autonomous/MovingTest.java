package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.FindMineralRunnable;
import org.firstinspires.ftc.teamcode.util.Position;

import java.util.Locale;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "Moving Test", group = "auto")
public class MovingTest extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private final double SPEED = 1d;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        telemetry.addLine("Finding Orientation of the gold mineral");
        telemetry.update();
        while(!opModeIsActive()){
            if(isStopRequested()) break;
            telemetry.addData("range", String.format(Locale.ENGLISH, "%.01f in", getFrontDSensor().getDistance(DistanceUnit.INCH)));
            telemetry.addData("range", String.format(Locale.ENGLISH, "%.01f in", getLeftDSensor().getDistance(DistanceUnit.INCH)));
            telemetry.addData("range", String.format(Locale.ENGLISH,  "%.01f in", getRightDSensor().getDistance(DistanceUnit.INCH)));
            telemetry.update();
        }
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.mecanumDriveBackward(10 , SPEED);
        driver.mecanumDriveForward(10, SPEED);
        driver.mecanumDriveRight(10, SPEED);
        driver.mecanumDriveLeft(10, SPEED);

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
