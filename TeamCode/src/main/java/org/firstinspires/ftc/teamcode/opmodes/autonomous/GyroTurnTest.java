package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.baseopmodes.AutonomousBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.RobotDriver;
import org.firstinspires.ftc.teamcode.util.Position;

import java.util.List;

/**
 * Ultro
 * AutonomousMode.java
 * Purpose: Extends from AutonomousBaseOpMode
 *
 * @version 1.0 10/11/2018
 */
@Autonomous(name = "Gyro Turn Test", group = "auto")
public class GyroTurnTest extends AutonomousBaseOpMode {
    RobotDriver driver = RobotDriver.getDriver();
    private Position position;
    private double originalAngle;
    private final double SPEED = 0.7;
    /*
    Before waitforStart()
     */
    @Override
    protected void prerun() {
        originalAngle = driver.correctAngle(driver.getCurrentAngle()); // stores the original angle, for later
        telemetry.addData("Angle", driver.getAngle());
        telemetry.addData("Correct Angle", driver.correctAngle(driver.getAngle()));
        resetStartTime();
    }
    /*
    After waitForStart()
     */
    @Override
    protected void run() {
        driver.gyroTurn(driver.getAngle(), 0.5, 5);
        sleep(750);
        driver.gyroTurn(-90, 0.5, 5);
        driver.mecanumDriveForward(-13, SPEED);
        getMarker().setPosition(1);
        sleep(2000);
        driver.gyroTurn(180,0.5,5);
        driver.mecanumDriveForward(-13, SPEED);
        position = Position.LEFT;
        switch (position){
            case LEFT:
                driver.mecanumDriveLeft(40, SPEED);
                break;
            case RIGHT:
                driver.mecanumDriveRight(40, SPEED);
                break;

        }
        driver.mecanumDriveForward(-25, SPEED);
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
